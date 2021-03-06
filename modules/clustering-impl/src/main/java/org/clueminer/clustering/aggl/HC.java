/*
 * Copyright (C) 2011-2019 clueminer.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clueminer.clustering.aggl;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.clueminer.clustering.algorithm.HClustResult;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.AlgParams;
import org.clueminer.clustering.api.Algorithm;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterLinkage;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.Configurator;
import org.clueminer.clustering.api.CutoffStrategy;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.clustering.api.InternalEvaluator;
import org.clueminer.clustering.api.config.annotation.Param;
import org.clueminer.clustering.api.dendrogram.DendroNode;
import org.clueminer.clustering.api.dendrogram.DendroTreeData;
import org.clueminer.clustering.api.factory.CutoffStrategyFactory;
import org.clueminer.clustering.api.factory.InternalEvaluatorFactory;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.hclust.DLeaf;
import org.clueminer.hclust.DTreeNode;
import org.clueminer.hclust.DynamicTreeData;
import org.clueminer.math.Matrix;
import org.clueminer.utils.PropType;
import org.clueminer.utils.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Hierarchical agglomerative clustering - base algorithm, same for all linkage
 * methods
 *
 * memory complexity:
 * <li>
 * <ul>double array (n - 1) * n / 2 - for storing similarity matrix</ul>
 * <ul>hash maps O(n^2)</ul>
 * <ul>tree structure (2 * n - 1 objects)</ul>
 * </li>
 * time complexity - omega n^2 * ( log n)
 *
 * Naive implementation, for real clustering use {@link HCLWMS}.
 *
 * Note: In order to avoid concurrency issues, the algorithm shouldn't keep
 * state
 *
 * @param <E>
 * @param <C>
 * @see
 * http://nlp.stanford.edu/IR-book/html/htmledition/time-complexity-of-hac-1.html
 * @author Tomas Barton
 */
public class HC<E extends Instance, C extends Cluster<E>> extends Algorithm<E, C> implements AgglomerativeClustering<E, C> {

    private final static String NAME = "HC";
    private static final Logger LOG = LoggerFactory.getLogger(HC.class);

    @Param(name = AlgParams.LINKAGE,
            factory = "org.clueminer.clustering.api.factory.LinkageFactory",
            type = org.clueminer.clustering.params.ParamType.STRING)
    protected ClusterLinkage linkage;

    @Param(name = AlgParams.CUTOFF_STRATEGY,
            factory = "org.clueminer.clustering.api.factory.CutoffStrategyFactory",
            type = org.clueminer.clustering.params.ParamType.STRING)
    protected CutoffStrategy cutoffStrategy;

    @Param(name = AlgParams.CUTOFF_SCORE,
            factory = "org.clueminer.clustering.api.factory.InternalEvaluatorFactory",
            type = org.clueminer.clustering.params.ParamType.STRING)
    protected InternalEvaluator cutoffScore;

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Computes hierarchical clustering with specified linkage and stores
     * dendrogram tree structure. However final clustering is not computed yet,
     * it will be formed later based on cut-off function.
     *
     * @param dataset
     * @param pref
     * @return
     */
    @Override
    public HierarchicalResult hierarchy(Dataset<E> dataset, Props pref) {
        int n;
        HierarchicalResult result = new HClustResult(dataset, pref);
        pref.put(AlgParams.ALG, getName());
        checkParams(pref);
        AlgParams params = new AlgParams(pref);
        distanceFunction = params.getDistanceMeasure();
        if (params.clusterRows()) {
            n = dataset.size();
        } else {
            //columns clustering
            n = dataset.attributeCount();
        }
        return hClust(dataset, dataset.asMatrix(), n, pref, params, result);
    }

    private HierarchicalResult hClust(Dataset<E> dataset, Matrix input, int n,
            Props pref, AlgParams params, HierarchicalResult result) {
        LOG.debug("{} clustering: {}", getName(), pref.toString());
        int items = triangleSize(n);
        //TODO: we might track clustering by estimated time (instead of counters)
        AbstractQueue<Element> pq = initQueue(items, pref);
        Matrix similarityMatrix;

        if (params.clusterRows()) {
            similarityMatrix = AgglClustering.rowSimilarityMatrix(input, distanceFunction, pq);
        } else {
            LOG.info("matrix columns: {}", input.columnsCount());
            similarityMatrix = AgglClustering.columnSimilarityMatrix(input, distanceFunction, pq);
        }
        //whether to keep reference to proximity matrix (could be memory exhausting)
        if (pref.getBoolean(PropType.PERFORMANCE, AlgParams.KEEP_PROXIMITY, false)) {
            result.setProximityMatrix(similarityMatrix);
        }

        DendroTreeData treeData = computeLinkage(pq, similarityMatrix, dataset, params, n);
        treeData.createMapping(n, treeData.getRoot());
        result.setTreeData(treeData);
        return result;
    }

    /**
     * Initialize processing queue
     *
     * @param items expected number of items in the queue
     * @param pref
     * @return
     */
    protected AbstractQueue<Element> initQueue(int items, Props pref) {
        AbstractQueue<Element> pq;
        //by default most similar items have smallest distance
        boolean smallestFirst = pref.getBoolean(AlgParams.SMALLEST_FIRST, true);
        if (smallestFirst) {
            pq = new PriorityQueue<>(items);
        } else {
            //inverse sorting - biggest values first
            Comparator<Element> comp = (Element o1, Element o2) -> o2.compareTo(o1);
            pq = new PriorityQueue<>(items, comp);
        }
        return pq;
    }

    public HierarchicalResult hierarchy(Matrix input, Dataset<E> dataset, Props pref) {
        int n;
        HierarchicalResult result = new HClustResult(dataset, pref);
        pref.put(AlgParams.ALG, getName());
        checkParams(pref);
        AlgParams params = new AlgParams(pref);
        distanceFunction = params.getDistanceMeasure();
        if (params.clusterRows()) {
            n = input.rowsCount();
        } else {
            //columns clustering
            n = input.columnsCount();
        }
        return hClust(dataset, input, n, pref, params, result);
    }

    /**
     * Could be overridden by inherited method to check where algorithm is
     * capable of running with requested parameters (otherwise throw an
     * Exception)
     *
     * @param props
     */
    protected void checkParams(Props props) {

    }

    /**
     * Find most closest items and merges them into one cluster (subtree)
     *
     * @param pq queue with sorted distances (lowest distance pops out first)
     * @param similarityMatrix
     * @param dataset
     * @param params
     * @param n number of items to cluster
     * @return
     */
    protected DendroTreeData computeLinkage(AbstractQueue<Element> pq, Matrix similarityMatrix, Dataset<E> dataset, AlgParams params, int n) {
        //binary tree, we know how many nodes we have
        DendroNode[] nodes = new DendroNode[(2 * n - 1)];
        //each instance will form a cluster
        Map<Integer, Set<Integer>> assignments = initialAssignment(n, dataset, params, nodes);

        Element curr;
        //HashSet<Integer> blacklist = new HashSet<>();
        final IntOpenHashSet blacklist = new IntOpenHashSet();
        //HashMap<Integer, Double> cache = new HashMap<>();
        Int2DoubleOpenHashMap cache = new Int2DoubleOpenHashMap();

        DendroNode node = null;
        Set<Integer> left, right;
        int nodeId = n;
        int ma, mb;
        HashMap<Integer, E> centroids = new HashMap<>();
        /**
         * queue of distances, each time join 2 items together, we should remove
         * (n-1) items from queue (but removing is too expensive)
         */
        while (!pq.isEmpty() && assignments.size() > 1) {
            curr = pq.poll();
            if (!blacklist.contains(curr.getRow()) && !blacklist.contains(curr.getColumn())) {
                node = getOrCreate(nodeId++, nodes);
                node.setLeft(nodes[curr.getRow()]);
                node.setRight(nodes[curr.getColumn()]);
                node.setHeight(curr.getValue());

                blacklist.add(curr.getRow());
                blacklist.add(curr.getColumn());

                //remove old clusters
                left = assignments.remove(curr.getRow());
                right = assignments.remove(curr.getColumn());
                ma = left.size();
                mb = right.size();
                //merge together and add as a new cluster
                left.addAll(right);
                updateDistances(node.getId(), left, similarityMatrix, assignments,
                        pq, params.getLinkage(), cache, curr.getRow(), curr.getColumn(),
                        ma, mb, centroids, dataset);
                //when assignment have size == 1, all clusters are merged into one
            }
        }

        //last node is the root
        DendroTreeData treeData = new DynamicTreeData(node);
        return treeData;
    }

    /**
     * Compute size of triangular matrix (n x n) minus diagonal
     *
     * @param n
     * @return
     */
    public int triangleSize(int n) {
        return ((n - 1) * n) >>> 1;
    }

    /**
     * Ensure obtaining node for given ID
     *
     * @param id
     * @param nodes
     * @return an inner node of the dendrogram tree
     */
    protected DendroNode getOrCreate(int id, DendroNode[] nodes) {
        if (nodes[id] == null) {
            DendroNode node = new DTreeNode(id);
            nodes[id] = node;
        }
        return nodes[id];
    }

    /**
     *
     * @param mergedId id of newly created cluster
     * @param mergedCluster id of all items in merged cluster
     * @param similarityMatrix matrix of distances
     * @param assignments
     * @param pq
     * @param linkage
     * @param cache
     * @param leftId left cluster ID
     * @param rightId right cluster ID
     * @param ma size of left cluster
     * @param mb size of right cluster
     * @param centroids
     * @param dataset
     */
    protected void updateDistances(int mergedId, Set<Integer> mergedCluster,
            Matrix similarityMatrix, Map<Integer, Set<Integer>> assignments,
            AbstractQueue<Element> pq, ClusterLinkage<E> linkage,
            Int2DoubleMap cache, int leftId, int rightId, int ma, int mb,
            HashMap<Integer, E> centroids, Dataset<? extends E> dataset) {
        Element current;
        double distance;
        Dataset<E> d = (Dataset<E>) dataset;

        E centroid = null;
        if (linkage.usesCentroids()) {
            E centroidA = fetchCentroid(leftId, centroids, d);
            E centroidB = fetchCentroid(rightId, centroids, d);
            centroid = linkage.updateCentroid(ma, mb, centroidA, centroidB, d);
            centroids.put(mergedId, centroid);
        }

        for (Map.Entry<Integer, Set<Integer>> cluster : assignments.entrySet()) {
            if (linkage.usesCentroids()) {
                distance = linkage.centroidDistance(ma, mb, centroid, fetchCentroid(cluster.getKey(), centroids, d));
            } else {
                distance = linkage.similarity(similarityMatrix, cluster.getValue(), mergedCluster);
            }

            current = new Element(distance, mergedId, cluster.getKey());

            pq.add(current);
        }
        //System.out.println("adding " + mergedId + " -> " + mergedCluster.toString());
        //finaly add merged cluster
        assignments.put(mergedId, mergedCluster);
    }

    private E fetchCentroid(int idx, HashMap<Integer, E> centroids, Dataset<E> dataset) {
        E centroid;
        if (idx < dataset.size()) {
            centroid = dataset.get(idx);
        } else {
            centroid = centroids.get(idx);
        }
        return centroid;
    }

    /**
     * Each data point forms an individual cluster
     *
     * @param n the number of data points
     * @param dataset
     * @param params
     * @param nodes
     * @return
     */
    protected Map<Integer, Set<Integer>> initialAssignment(int n, Dataset<? extends Instance> dataset,
            AlgParams params, DendroNode[] nodes) {
        Map<Integer, Set<Integer>> clusterAssignment = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            //cluster contain all its members (in final step, its size is equal to n)
            HashSet<Integer> cluster = new HashSet<>();
            cluster.add(i);
            clusterAssignment.put(i, cluster);
            //each cluster is also a dendrogram leaf
            if (params.clusterRows()) {
                nodes[i] = new DLeaf(i, dataset.get(i));
            } else {
                nodes[i] = new DLeaf(i, dataset.getAttribute(i));
            }

        }
        return clusterAssignment;
    }

    @Override
    public boolean isLinkageSupported(String linkage) {
        switch (linkage) {
            case "Ward's Linkage":
                return false;
            default:
                return true;
        }
    }

    @Override
    public Clustering<E, C> cluster(Dataset<E> dataset, Props props) {
        HierarchicalResult hres = hierarchy(dataset, props);
        CutoffStrategy strategy = getCutoffStrategy(props);
        double cutoff = hres.findCutoff(strategy);
        LOG.info("found cutoff at {} using {}", cutoff, strategy.getName());
        return hres.getClustering();
    }

    public CutoffStrategy<E, C> getCutoffStrategy(Props params) {
        CutoffStrategy<E, C> strategy;
        String cutoffAlg = params.get(AlgParams.CUTOFF_STRATEGY, "(none)");

        if (cutoffAlg.equals("(none)")) {
            strategy = CutoffStrategyFactory.getInstance().getDefault();
        } else {
            strategy = CutoffStrategyFactory.getInstance().getProvider(cutoffAlg);
        }
        if (params.containsKey(AlgParams.CUTOFF_SCORE)) {
            String evalAlg = params.get(AlgParams.CUTOFF_SCORE);
            InternalEvaluatorFactory<E, C> ief = InternalEvaluatorFactory.getInstance();
            InternalEvaluator<E, C> eval = ief.getProvider(evalAlg);
            strategy.setEvaluator(eval);
        }
        return strategy;
    }

    @Override
    public Configurator<E> getConfigurator() {
        return HCConfig.getInstance();
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

}
