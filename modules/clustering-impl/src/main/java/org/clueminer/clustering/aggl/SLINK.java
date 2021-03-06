/*
 * Copyright (C) 2011-2018 clueminer.org
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.clueminer.clustering.algorithm.HClustResult;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.AlgParams;
import org.clueminer.clustering.api.Algorithm;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.Configurator;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.api.Distance;
import org.clueminer.utils.Dump;
import org.clueminer.utils.MapUtils;
import org.clueminer.utils.Props;

/**
 * SLINK clustering - also known as nearest neighbour clustering (a variant of
 * hierarchical clustering algorithm with single linkage)
 *
 * R. Sibson (1973). "SLINK: an optimally efficient algorithm for the
 * single-link cluster method". The Computer Journal (British Computer Society)
 * 16 (1): 30–34. doi:10.1093/comjnl/16.1.30.
 *
 * @author Tomas Barton
 * @param <T>
 */
public class SLINK<E extends Instance, C extends Cluster<E>> extends Algorithm<E, C> implements AgglomerativeClustering<E, C> {

    public static final String name = "SLINK";

    @Override
    public String getName() {
        return name;
    }

    public PointerHierarchy run(Dataset<? extends Instance> dataset, Props pref) {
        AlgParams params = new AlgParams(pref);
        int[] processed = new int[dataset.size()];
        //storage for distances
        double[] m = new double[dataset.size()];

        //pi = new HashMap<>(dataset.size());
        //lambda = new HashMap<>(dataset.size());
        int[] pi = new int[dataset.size()];
        //we need to sort indexes by lambdas, it's better to have it in a map
        Map<Integer, Double> lambda = new HashMap<>(dataset.size());
        Distance dm = params.getDistanceMeasure();

        int i = 0, id;
        for (Instance inst : dataset) {
            id = inst.getIndex();
            step1(id, lambda, pi);
            step2(id, processed, i, dm, m, dataset);
            step3(id, processed, i, dm, lambda, pi, m);
            step4(id, processed, i, lambda, pi);
            processed[i] = id;
            i++;
        }

        //we don't need m anymore
        m = null;
        return new PointerHierarchy(dataset, lambda, pi);
    }

    @Override
    public HierarchicalResult hierarchy(Dataset<E> dataset, Props pref) {

        HierarchicalResult result = new HClustResult(dataset, pref);

        PointerHierarchy pointers = run(dataset, pref);

        extractClusters(pointers);

        return result;
    }

    private void step1(int id, Map<Integer, Double> lambda, int[] pi) {
        //pi.put(id, id);
        //lambda.put(id, Double.POSITIVE_INFINITY);
        // P(n+1) = n+1:
        pi[id] = id;
        // L(n+1) = infinity
        lambda.put(id, Double.POSITIVE_INFINITY);
    }

    private void step2(int newId, int[] processed, int i, Distance dm, double[] m, Dataset<? extends Instance> dataset) {
        for (int j = 0; j < i; j++) {
            // M(i) = dist(i, n+1)
            m[j] = dm.measure(dataset.get(newId), dataset.get(processed[j]));
        }

    }

    private void step3(int newId, int[] processed, int i, Distance dm,
            Map<Integer, Double> lambda, int[] pi, double[] m) {
        double l_i, m_i, mp_i;
        int p_i, id;
        for (int j = 0; j < i; j++) {
            id = processed[j];
            l_i = lambda.get(id);
            m_i = m[id];
            p_i = pi[id];
            mp_i = m[p_i];

            // if L(i) >= M(i)
            //TODO: replace by dm.compare
            if (l_i >= m_i) {
                // M(P(i)) = min { M(P(i)), L(i) }
                m[p_i] = Math.min(mp_i, l_i);

                // L(i) = M(i)
                lambda.put(id, m_i);

                // P(i) = n+1;
                pi[id] = newId;
            } else {
                // M(P(i)) = min { M(P(i)), M(i) }
                m[p_i] = Math.min(mp_i, m_i);
            }
        }
    }

    /**
     * Update clusters if it is necessary
     *
     * @param newId
     * @param processed
     * @param i
     * @param lambda
     * @param pi
     */
    private void step4(int newId, int[] processed, int i, Map<Integer, Double> lambda, int[] pi) {
        int id;
        double l_i, lp_i;
        for (int j = 0; j < i; j++) {
            id = processed[j];
            l_i = lambda.get(id);
            lp_i = lambda.get(pi[id]);
            // if L(i) >= L(P(i))
            if (l_i >= lp_i) {
                // P(i) = n+1
                pi[id] = newId;
            }
        }
    }

    private void extractClusters(PointerHierarchy pointers) {
        Dataset<? extends Instance> dataset = pointers.getDataset();
        Map<Integer, Double> lambda = pointers.getLambda();
        int[] pi = pointers.getPi();
        Map<Integer, Double> order = MapUtils.sortByValue(lambda);
        Dump.map(order, "order");

        int expcnum = dataset.size();
        int[] cluster_map = new int[dataset.size()];
        Arrays.fill(cluster_map, 0, dataset.size(), -1);
        Map<Integer, Set<Integer>> cluster_dbids = new HashMap<>(expcnum);
        double[] cluster_dist = new double[expcnum];
        int[] cluster_leads = new int[expcnum];

        int clustNum = 0;
        int succ;
        // Go backwards on the lower part.
        for (int i = 0; i < order.size(); i++) {
            double dist = order.get(i); // Distance to successor
            succ = pi[i];
            int clusterid = cluster_map[succ];
            // Successor cluster has already been created:
            if (clusterid >= 0) {
                cluster_dbids.get(clusterid).add(i);
                cluster_map[i] = clusterid;
                // Update distance to maximum encountered:
                if (cluster_dist[clusterid] < dist) {
                    cluster_dist[clusterid] = dist;
                }
            } else {
                // Need to start a new cluster:
                clusterid = clustNum++; // next cluster number.
                Set<Integer> cids = new LinkedHashSet<>();
                // Add element and successor as initial members:
                cids.add(succ);
                cluster_map[succ] = clusterid;
                cids.add(i);
                cluster_map[i] = clusterid;
                // Store new cluster.
                cluster_dbids.put(clusterid, cids);
                cluster_leads[clusterid] = succ;
                cluster_dist[clusterid] = dist;
            }
        }
        System.out.println(cluster_dbids.toString());
        Dump.array(cluster_dist, "cluster_dist");
        Dump.array(cluster_map, "cluster_map");
        Dump.array(cluster_leads, "cluster_leads");

    }

    @Override
    public Clustering<E, C> cluster(Dataset<E> dataset, Props props) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLinkageSupported(String linkage) {
        return linkage.equals("Single");
    }

    @Override
    public Configurator<E> getConfigurator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

}
