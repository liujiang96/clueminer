package org.clueminer.evolution.hac;

import java.util.List;
import org.clueminer.clustering.aggl.AgglParams;
import org.clueminer.clustering.aggl.HAC;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterEvaluator;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.clustering.api.dendrogram.DendrogramMapping;
import org.clueminer.clustering.api.evolution.Evolution;
import org.clueminer.clustering.struct.DendrogramData;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.eval.hclust.HillClimbCutoff;
import org.clueminer.evolution.AbstractEvolution;
import org.clueminer.math.Matrix;
import org.clueminer.math.StandardisationFactory;
import org.clueminer.std.Scaler;
import org.clueminer.utils.Props;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = Evolution.class)
public class NormalizationEvolution extends AbstractEvolution implements Runnable, Evolution, Lookup.Provider {

    private static final String name = "Normalizations";
    private AgglomerativeClustering algorithm;
    private int gen;

    public NormalizationEvolution() {
        instanceContent = new InstanceContent();
        lookup = new AbstractLookup(instanceContent);
        //TODO allow changing algorithm used
        algorithm = new HAC();
        gen = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    private void prepare() {
        if (dataset == null) {
            throw new RuntimeException("missing data");
        }
    }

    @Override
    public void run() {
        prepare();

        Props params;
        StandardisationFactory sf = StandardisationFactory.getInstance();
        List<String> standartizations = sf.getProviders();

        int stdMethods = standartizations.size();

        if (ph != null) {
            ph.start(stdMethods * 2);
            ph.progress("starting evolution...");
        }
        int i = 0;
        for (String std : standartizations) {
            params = new Props();
            //no log scale
            makeClusters(std, false, params, i);
            //with log scale
            makeClusters(std, true, params, i);
        }

        finish();
    }

    /**
     * Make clusters - not war
     *
     * @param std
     * @param logscale
     * @param params
     * @param i
     */
    protected void makeClusters(String std, boolean logscale, Props params, int i) {
        Clustering<? extends Cluster> clustering;
        Matrix input = standartize(dataset, std, logscale);
        params.put("algorithm", algorithm.getName());
        params.putBoolean("logscale", logscale);
        params.put("std", std);
        params.putBoolean(AgglParams.CLUSTER_ROWS, true);
        HierarchicalResult rowsResult = algorithm.hierarchy(input, dataset, params);
        HillClimbCutoff strategy = new HillClimbCutoff((ClusterEvaluator) evaluator);
        params.put("cutoff-score", evaluator.getName());
        rowsResult.findCutoff(strategy);
        clustering = rowsResult.getClustering();
        clustering.mergeParams(params);
        DendrogramMapping mapping = new DendrogramData(dataset, input, rowsResult);
        clustering.lookupAdd(mapping);
        individualCreated(clustering);
        if (ph != null) {
            ph.progress(i++);
        }
    }

    public Matrix standartize(Dataset<? extends Instance> data, String method, boolean logScale) {
        return Scaler.standartize(data.arrayCopy(), method, logScale);
    }

    protected void finish() {
        if (ph != null) {
            ph.finish();
        }
    }

    protected void individualCreated(Clustering<? extends Cluster> clustering) {
        instanceContent.add(clustering);
        fireBestIndividual(gen++, new BaseIndividual(clustering), getEvaluator().score((Clustering<Cluster>) clustering, dataset));
    }

}