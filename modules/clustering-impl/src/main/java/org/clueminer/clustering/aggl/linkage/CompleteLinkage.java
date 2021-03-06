package org.clueminer.clustering.aggl.linkage;

import java.util.Set;
import org.clueminer.clustering.api.AbstractLinkage;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterLinkage;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.EuclideanDistance;
import org.clueminer.distance.api.Distance;
import org.clueminer.math.Matrix;
import org.openide.util.lookup.ServiceProvider;

/**
 * We consider the distance between one cluster and another to be equal to the
 * greatest distance from any member of first cluster to any member of second
 * cluster. Sometimes it's called the maximum method or the diameter method.
 *
 * Complete Link or MAX or CLIQUE
 *
 * @author Tomas Barton
 * @param <E>
 */
@ServiceProvider(service = ClusterLinkage.class)
public class CompleteLinkage<E extends Instance> extends AbstractLinkage<E> implements ClusterLinkage<E> {

    private static final long serialVersionUID = -852898753773273748L;
    public static final String name = "Complete";

    public CompleteLinkage() {
        super(EuclideanDistance.getInstance());
    }

    public CompleteLinkage(Distance dm) {
        super(dm);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double distance(Cluster<E> cluster1, Cluster<E> cluster2) {
        double maximumDistance = Double.MIN_VALUE;
        for (Instance i : cluster1) {
            for (Instance j : cluster2) {
                double s = distanceMeasure.measure(i, j);
                if (distanceMeasure.compare(maximumDistance, s)) {
                    maximumDistance = s;
                }
            }
        }
        return maximumDistance;
    }

    /**
     * For the complete link or MAX version of hierarchical clustering, the
     * proximity of two clusters is defined as the maximum of the distance
     * (minimum of the similarity) between any two points in the two different
     * clusters.
     *
     * @param similarityMatrix
     * @param cluster
     * @param toAdd
     * @return
     */
    @Override
    public double similarity(Matrix similarityMatrix, Set<Integer> cluster, Set<Integer> toAdd) {
        double maximumDistance = Double.MIN_VALUE;
        for (int i : cluster) {
            for (int j : toAdd) {
                double s = similarityMatrix.get(i, j);
                if (distanceMeasure.compare(maximumDistance, s)) {
                    maximumDistance = s;
                }
            }
        }
        return maximumDistance;
    }

    @Override
    public double alphaA(int ma, int mb, int mq) {
        return 0.5;
    }

    @Override
    public double alphaB(int ma, int mb, int mq) {
        return 0.5;
    }

    @Override
    public double beta(int ma, int mb, int mq) {
        return 0.0;
    }

    @Override
    public double gamma() {
        return 0.5;
    }

    @Override
    public boolean usesCentroids() {
        return false;
    }

    @Override
    public E updateCentroid(int ma, int mb, E centroidA, E centroidB, Dataset<E> dataset) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double centroidDistance(int ma, int mb, E centroidA, E centroidB) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
