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
package org.clueminer.evaluation.inline;

import org.clueminer.eval.sort.MoEvaluator;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.clueminer.clustering.api.AlgParams;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.EvaluationTable;
import org.clueminer.clustering.api.Rank;
import org.clueminer.clustering.api.ScoreException;
import org.clueminer.clustering.api.dendrogram.ColorScheme;
import org.clueminer.clustering.api.factory.Clusterings;
import org.clueminer.clustering.api.factory.RankEvaluatorFactory;
import org.clueminer.clustering.gui.colors.ColorSchemeImpl;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.eval.AIC;
import org.clueminer.eval.external.NMIsqrt;
import org.clueminer.eval.utils.ClusteringComparator;
import org.clueminer.eval.utils.HashEvaluationTable;
import org.clueminer.gui.BPanel;
import org.clueminer.std.StdScale;
import org.clueminer.utils.Props;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.clueminer.clustering.api.RankEvaluator;
import org.clueminer.clustering.api.factory.RankFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author deric
 */
public class ScorePlot<E extends Instance, C extends Cluster<E>> extends BPanel implements TaskListener {

    private static final long serialVersionUID = -4456572592761477081L;

    private Collection<? extends Clustering> clusterings;
    private Clustering[] internal;
    private Clustering[] external;
    private ClusteringComparator compExternal;
    private List<ClusterEvaluation<E, C>> objectives;
    private final MoEvaluator moEval;
    private ClusterEvaluation<E, C> soEval;
    protected Font defaultFont;
    protected Font headerFont;
    protected int lineHeight = 12;
    protected int elemHeight = 20;
    protected int fontSize = 14;
    private int headerHeight;
    protected float headerFontSize = 10;
    private int maxWidth;
    private Insets insets = new Insets(15, 15, 10, 15);
    static BasicStroke wideStroke = new BasicStroke(8.0f);
    private ColorScheme colorScheme;
    private static final RequestProcessor RP = new RequestProcessor("sorting...", 100, false, true);
    private Color fontColor;
    private final StdScale scale;
    private int scaleTickLength = 6;
    protected DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private int labelOffset = 13;
    public Clustering<E, C> goldenStd;
    private int rectWidth = 10;
    private boolean useActualMetricMax = true;
    private boolean crossAtMedian = true;
    private boolean showCorrelation = true;
    private static final String GROUND_TRUTH = "ground-truth";
    private double correlation = Double.NaN;
    private RankEvaluator rankEval;
    private Rank rank;
    private final HashMap<Integer, Integer> extMap;
    private static final Logger LOG = LoggerFactory.getLogger(ScorePlot.class);
    private Lock internalLock = new ReentrantLock();

    public ScorePlot() {
        defaultFont = new Font("verdana", Font.PLAIN, fontSize);
        headerFont = defaultFont.deriveFont(Font.BOLD);
        scale = new StdScale();
        this.fitToSpace = false;
        this.preserveAlpha = true;
        compExternal = new ClusteringComparator(new NMIsqrt());
        //colorScheme = new ColorSchemeImpl(Color.RED, Color.BLACK, Color.GREEN);
        colorScheme = new ColorSchemeImpl(Color.GREEN, Color.BLACK, Color.RED);
        try {
            initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Exceptions.printStackTrace(ex);
        }
        moEval = new MoEvaluator();
        soEval = new AIC();
        rank = RankFactory.getInstance().getDefault();
        rankEval = RankEvaluatorFactory.getInstance().getDefault();
        extMap = new HashMap<>();
        objectives = new LinkedList();
        objectives.add(soEval);
    }

    private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        fontColor = defaults.getColor("infoText");
        //setBackground(defaults.getColor("window"));
        //this.preserveAlpha = true;
        setBackground(defaults.getColor("window"));
    }

    protected void setEvaluatorX(final ClusterEvaluation provider) {
        if (external != null && external.length > 1 && provider != null) {
            final ProgressHandle ph = ProgressHandle.createHandle("computing " + provider.getName());
            RequestProcessor.Task task = RP.post(new Runnable() {

                @Override
                public void run() {
                    ph.start();
                    ClusteringComparator compare = new ClusteringComparator(provider);
                    try {
                        Arrays.parallelSort(external, compare);
                    } catch (IllegalArgumentException e) {
                        System.err.println("sorting error during " + provider.getName());
                        double[] score = new double[external.length];
                        EvaluationTable et;
                        for (int i = 0; i < score.length; i++) {
                            et = compare.evaluationTable(external[i]);
                            score[i] = et.getScore(provider);
                        }
                        System.out.println(Arrays.toString(score));
                    }
                    compExternal.setEvaluator(provider);
                    updateExtMapping();
                    ph.finish();
                }
            });
            task.addTaskListener(this);
        }
    }

    /**
     * create mapping for easier finding reference solution by its ID
     */
    private void updateExtMapping() {
        extMap.clear();
        for (int i = 0; i < external.length; i++) {
            extMap.put(external[i].getId(), i);
        }
    }

    public void setClusterings(final Collection<Clustering> clusters) {
        RequestProcessor.Task task = RP.post(new Runnable() {

            @Override
            public void run() {
                goldenStd = goldenStandard(clusters);
                if (goldenStd != null) {
                    goldenStd.getParams().put(AlgParams.ALG, GROUND_TRUTH);
                    internal = clusters.toArray(new Clustering[clusters.size() + 1]);
                    internal[clusters.size()] = goldenStd;
                    external = clusters.toArray(new Clustering[clusters.size() + 1]);
                    external[clusters.size()] = goldenStd;
                } else {
                    internal = clusters.toArray(new Clustering[clusters.size()]);
                    external = clusters.toArray(new Clustering[clusters.size()]);
                }
                //race condition, needs to be sorted individually
                //Arrays.parallelSort(internal, rank.getComparator());
                Arrays.parallelSort(external, compExternal);
                clusterings = clusters;
                updateExtMapping();
            }
        });
        task.addTaskListener(this);
    }

    @Override
    public void taskFinished(Task task) {
        clusteringChanged();
    }

    protected void clusteringChanged() {
        if (hasData()) {
            resetCache();
        }
    }

    /**
     * Fast correlation computation
     *
     * @return
     */
    protected double updateCorrelation() {
        LOG.debug("updating correlation");
        if (rankEval != null) {
            return rankEval.correlation(external, internal, extMap);
        }
        return Double.NaN;
    }

    private Clustering<E, C> goldenStandard(Collection<Clustering> clusters) {
        Clustering<E, C> golden = null;
        if (clusters != null && !clusters.isEmpty()) {
            Clustering<E, C> clust = clusters.iterator().next();
            Dataset<E> dataset = clust.getLookup().lookup(Dataset.class);
            if (dataset != null) {
                SortedSet set = dataset.getClasses();
                golden = (Clustering<E, C>) Clusterings.newList();
                golden.lookupAdd(dataset);
                EvaluationTable evalTable = new HashEvaluationTable(golden, dataset);
                golden.lookupAdd(evalTable);
                HashMap<Object, Integer> map = new HashMap<>(set.size());
                Object obj;
                Iterator it = set.iterator();
                int i = 0;
                Cluster c;
                while (it.hasNext()) {
                    obj = it.next();
                    c = golden.createCluster(i);
                    c.setAttributes(dataset.getAttributes());
                    map.put(obj, i++);
                }
                int assign;

                for (E inst : dataset) {
                    if (inst.classValue() == null) {
                        LOG.error("null class for inst {}", inst.getIndex());
                    } else {
                        if (map.containsKey(inst.classValue())) {
                            assign = map.get(inst.classValue());
                            c = golden.get(assign);
                        } else {
                            c = golden.createCluster(i);
                            c.setAttributes(dataset.getAttributes());
                            map.put(inst.classValue(), i++);
                        }
                        c.add(inst);
                    }
                }
            }
        }
        return golden;
    }

    /**
     * Find worst value in a sorted array which is a number and could be plotted
     *
     * @param clust
     * @param comp
     * @param ref
     * @return
     */
    private double scoreWorst(Clustering[] clust, ClusterEvaluation<E, C> comp) throws ScoreException {
        double res = Double.NaN;
        if (comp != null && clust != null && clust.length > 0) {
            int i = 0;
            do {
                res = comp.score(clust[i++]);
            } while (!isFinite(res) && i < clust.length);
        }
        return res;
    }

    /**
     * Find best score value in an array of clusterings which has a quality of a
     * number (not NaN, INFINIFY, ...)
     *
     * @param clust
     * @param comp
     * @return
     */
    private double scoreBest(Clustering[] clust, ClusterEvaluation<E, C> comp) throws ScoreException {
        double res = Double.NaN;

        if (comp != null && clust != null && clust.length > 0) {
            int i = clust.length - 1;
            do {
                res = comp.score(clust[i--]);
            } while (!isFinite(res) && i >= 0);
        }

        return res;
    }

    @Override
    public void render(Graphics2D g) {
        if (g == null || rank.getEvaluator() == null) {
            return;
        }
        try {
            this.g = g;
            double xmin, xmax, ymin, ymax, ymid;
            if (rank.getEvaluator() instanceof MoEvaluator) {
                xmin = 0;
                xmax = internal.length - 1;
            } else {
                xmin = scoreBest(internal, rank.getEvaluator());
                xmax = scoreWorst(internal, rank.getEvaluator());
                //LOG.info("worst value: {}", xmax);
            }   //xmid = (xmax - xmin) / 2.0 + xmin;
            ymin = scoreBest(external, compExternal.getEvaluator());
            ymax = scoreWorst(external, compExternal.getEvaluator());
            //System.out.println("ymin= " + ymin + ", ymax= " + ymax);
            if (crossAtMedian && external != null && external.length > 2) {
                int pos = (external.length / 2);
                try {
                    ymid = compExternal.getEvaluator().score(external[pos]);
                } catch (ScoreException ex) {
                    ymid = 0.0;
                    LOG.warn("failed to compute{}: {}", compExternal.getEvaluator().getName(), ex.getMessage());
                }
            } else {
                ymid = (ymax - ymin) / 2.0 + ymin;
            }
            int cxMin, cxMax, cyMin, cyMax;
            cxMin = insets.left + 20;
            cyMin = insets.top + 15;
            cyMax = getSize().height - insets.bottom;
            int cyMid = (int) scale.scaleToRange(ymid, ymin, ymax, cyMin, cyMax);
            //LOG.info("rank {}, eval: {}", rank.getName(), rank.getEvaluator());
            cxMax = drawXLabel(g, rank.getEvaluator().getName(), getSize().width - insets.right, cyMid);
            int cxMid = (int) ((cxMax - cxMin) / 2) + cxMin;
            drawYLabel(g, compExternal.getEvaluator().getName(), cyMin, cxMid);
            //if we have clear bounds, use them
            if (isFinite(compExternal.getEvaluator().getMin())) {
                //for purpose of visualization min and max are reversed
                if (!useActualMetricMax) {
                    ymax = compExternal.getEvaluator().getMin();
                }
            }
            if (isFinite(compExternal.getEvaluator().getMax())) {
                //for purpose of visualization min and max are reversed
                if (!useActualMetricMax) {
                    ymin = compExternal.getEvaluator().getMax();
                }
            }   //set font for rendering rows
            g.setFont(defaultFont);
            double xVal, yVal, score, hypo, diff;
            //draw
            Rectangle2D rect;
            Props p;
            for (Clustering clust : external) {
                //left clustering
                score = rank.getEvaluator().score(clust);
                xVal = scale.scaleToRange(score, xmin, xmax, cxMin, cxMax) - rectWidth / 2;
                score = compExternal.getScore(clust);

                //color according to position difference to external score placement
                hypo = scale.scaleToRange(score, ymin, ymax, cxMin, cxMax) - rectWidth / 2;
                diff = Math.abs(xVal - hypo);
                //last one is min rect. height
                yVal = scale.scaleToRange(score, ymin, ymax, cyMin, cyMax);
                p = clust.getParams();
                if (p != null && p.get(AlgParams.ALG, "foo bar alg").equals(GROUND_TRUTH)) {
                    g.setComposite(AlphaComposite.SrcOver.derive(0.8f));
                    g.setColor(Color.YELLOW);
                } else {
                    g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                    //g.setColor(colorScheme.getColor(diff, ymin, ymid, ymax));
                    g.setColor(colorScheme.getColor(diff, cxMin, cxMid, cxMax));
                }
                if (yVal < cyMid) {
                    rect = new Rectangle2D.Double(xVal, yVal, rectWidth, cyMid - yVal);
                } else {
                    rect = new Rectangle2D.Double(xVal, cyMid, rectWidth, yVal - cyMid);
                }
                g.fill(rect);
                g.draw(rect);
                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(Color.black);
                //drawNumberX(score, (int) xVal, (int) yVal);
                //drawNumberX(diff, (int) xVal, (int) yVal);
            }
            g.setColor(fontColor);
            drawHorizontalScale(g, cxMin, cxMax, cyMid, xmin, xmax);
            drawVerticalScale(g, cyMin, cyMax, cxMid, ymin, ymax);
            if (showCorrelation) {
                if (!Double.isNaN(correlation)) {
                    //LOG.debug("drawing correlation: {}", correlation);
                    drawNumberX(correlation, cxMax, cyMin);
                }
            }   //average distance per item
            g.dispose();
        } catch (ScoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void drawVerticalScale(Graphics2D g, int cyMin, int cyMax, int xPos, double scMin, double scMax) {
        g.setColor(Color.black);
        g.drawLine(xPos, cyMin, xPos, cyMax);

        //min
        g.drawLine(xPos - scaleTickLength / 2, cyMin, xPos + scaleTickLength / 2, cyMin);
        drawNumberY(scMin, xPos + scaleTickLength, cyMin, g.getFontMetrics());
        //max
        g.drawLine(xPos - scaleTickLength / 2, cyMax, xPos + scaleTickLength / 2, cyMax);
        drawNumberY(scMax, xPos + scaleTickLength, cyMax, g.getFontMetrics());
    }

    private void drawHorizontalScale(Graphics2D g, int cxMin, int cxMax, int yPos, double scMin, double scMax) {
        g.setColor(Color.black);
        g.drawLine(cxMin, yPos, cxMax, yPos);

        //min
        g.drawLine(cxMin, yPos - scaleTickLength / 2, cxMin, yPos + scaleTickLength / 2);
        drawNumberX(scMin, cxMin, yPos + scaleTickLength / 2 + labelOffset);
        //max
        g.drawLine(cxMax, yPos - scaleTickLength / 2, cxMax, yPos + scaleTickLength / 2);
        drawNumberX(scMax, cxMax, yPos + scaleTickLength / 2 + labelOffset);
    }

    private synchronized void drawNumberX(double value, int x, int y) {
        String lb = decimalFormat.format(value);
        int sw = stringWidth(defaultFont, g, lb);
        //center the number
        g.drawString(lb, x - sw / 2, y);
    }

    private synchronized void drawNumberY(double value, int x, int y, FontMetrics hfm) {
        String lb = decimalFormat.format(value);

        //center the number
        g.drawString(lb, x, y + hfm.getHeight() / 2 - hfm.getDescent());
    }

    /**
     * Compute string width for given string
     *
     * @param f
     * @param g2
     * @param str
     * @return
     */
    private int stringWidth(Font f, Graphics2D g2, String str) {
        if (f != null && g2 != null && g2.getFontRenderContext() != null) {
            return (int) (f.getStringBounds(str, g2.getFontRenderContext()).getWidth());
        } else {
            return 10;
        }
    }

    /**
     *
     * @param g2
     * @param label
     * @param xmax
     * @param ymid
     * @return position where should x axis end
     */
    private int drawXLabel(Graphics2D g2, String label, int xmax, int ymid) {
        g2.setColor(fontColor);
        g2.setFont(defaultFont);
        int strWidth = stringWidth(defaultFont, g2, label);
        int x = xmax - strWidth - 5;
        int y = (int) (ymid - defaultFont.getSize() - g2.getFontMetrics().getDescent() * 2.0);
        g2.drawString(label, x, y);
        return x;
    }

    private int drawYLabel(Graphics2D g2, String label, int ymax, int xmid) {
        g2.setColor(fontColor);
        g2.setFont(defaultFont);
        int strHeight = g2.getFontMetrics().getHeight() - g2.getFontMetrics().getDescent();
        int strWidth = stringWidth(defaultFont, g2, label);
        int y = ymax - strHeight;
        int x = (int) (xmid - strWidth / 2.0);
        g2.drawString(label, x, y);
        return x;
    }

    private void drawDistance(Graphics2D g2, double distance) {
        g2.setColor(fontColor);
        int colWidth = getSize().width / 3;
        String str = String.format("%.2f", distance) + " (" + clusterings.size() + ")";
        g2.setFont(headerFont);
        int strWidth = stringWidth(headerFont, g, str);
        // 2nd column
        int x = colWidth + (colWidth - strWidth) / 2;
        int y = (int) (headerFontSize + g.getFontMetrics().getDescent() * 2);
        g.drawString(str, x, y);
    }

    private void drawClustering(Graphics2D g, Clustering clust, int rectWidth, double xVal, double yVal, int mid) {
        String str = clust.getName();
        int width;
        int x, y;
        if (str == null) {
            str = "unknown |" + clust.size() + "|";
        }
        g.setFont(defaultFont);

        x = (int) xVal;
        // y = (int) (mid - yVal);

        width = stringWidth(defaultFont, g, str);
        checkMax(width);
        y = (int) (x + g.getFontMetrics().getDescent() / 2f);
        g.drawString(str, x, y);
    }

    private void checkMax(int width) {
        if (width > maxWidth) {
            maxWidth = width;
            resetCache();
        }
    }

    @Override
    public void sizeUpdated(Dimension size) {
        if (hasData()) {
            //use maximum width avaiable
            realSize.width = size.width;
            maxWidth = 0;
            resetCache();
        }
    }

    /**
     * Could be replace by Double.isFinite which is available in Java 8
     *
     * @param d
     * @return
     */
    public boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    @Override
    public boolean hasData() {
        return internal != null && external != null;
    }

    @Override
    public void recalculate() {
        //int width = 40 + maxWidth;
        int height = headerHeight;
        //elemHeight = (realSize.height - insets.top - insets.bottom) / itemsCnt();
        //if (elemHeight > lineHeight) {
        if (clusterings != null) {
            height += elemHeight * clusterings.size();
            //}
            //realSize.width = width;
            //reqSize.width = width;
            realSize.height = height;
            //reqSize.height = height;
            //setMinimumSize(realSize);
            setPreferredSize(realSize);
            //setSize(realSize);
        }
    }

    @Override
    public boolean isAntiAliasing() {
        return true;
    }

    public Dataset<? extends Instance> getDataset() {
        if (clusterings != null && clusterings.size() > 0) {
            Clustering c = clusterings.iterator().next();
            return c.getLookup().lookup(Dataset.class);
        }
        return null;
    }

    public Collection<? extends Clustering> getClusterings() {
        return clusterings;
    }

    public boolean isUseSupervisedMetricMax() {
        return useActualMetricMax;
    }

    public void setUseSupervisedMetricMax(boolean useSupervisedMetricMax) {
        this.useActualMetricMax = useSupervisedMetricMax;
    }

    public void setCrossAxisAtMedian(boolean crossAtMedian) {
        this.crossAtMedian = crossAtMedian;
    }

    public void setShowCorrelation(boolean show) {
        this.showCorrelation = show;
    }

    public void setRankEvaluator(RankEvaluator rank) {
        this.rankEval = rank;
    }

    public void setRank(Rank ranking) {
        this.rank = ranking;
    }

    public void computeRanking() {
        if (internal != null && internal.length > 1) {
            final ProgressHandle ph = ProgressHandle.createHandle("computing " + rank.getName() + "(" + printObjectives() + ")");
            RP.post(new Runnable() {

                @Override
                public void run() {
                    ph.start();
                    internalLock.lock();
                    try {
                        internal = rank.sort(internal, objectives);
                        correlation = updateCorrelation();
                    } finally {
                        internalLock.unlock();
                    }
                    LOG.info("using {}({}), corr: {}", rank.getName(), printObjectives(), correlation);
                    clusteringChanged();
                    //dumpInternal();
                    ph.finish();
                }
            });
        }
    }

    protected void dumpInternal() {
        for (int i = 0; i < internal.length; i++) {
            try {
                Clustering clustering = internal[i];
                LOG.debug("{}: {}", clustering.fingerprint(), rank.getEvaluator().score(clustering));
            } catch (ScoreException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public void setObjectives(List<ClusterEvaluation<E, C>> objectives) {
        this.objectives = objectives;
        if (rank.isMultiObjective()) {
            moEval.setObjectives(objectives);
        } else {
            soEval = objectives.get(0);
        }
    }

    private String printObjectives() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ClusterEvaluation ce : objectives) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(ce.getName());
            i++;
        }
        return sb.toString();
    }

}
