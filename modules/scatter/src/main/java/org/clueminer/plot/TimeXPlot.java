/*
 * Copyright (C) 2011-2017 clueminer.org
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
package org.clueminer.plot;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.XChartPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.clueminer.dataset.api.ContinuousInstance;
import org.clueminer.dataset.api.DataType;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.Plotter;
import org.clueminer.dataset.api.Timeseries;
import org.clueminer.dataset.impl.InstCollection;
import org.openide.util.lookup.ServiceProvider;
import smile.neighbor.LSH;
import smile.neighbor.Neighbor;

/**
 * Plot for rendering timeseries data.
 *
 * Low number position service are returned first.
 *
 * @author deric
 * @param <E>
 */
@ServiceProvider(service = Plotter.class, position = 10)
public class TimeXPlot<E extends Instance> extends JPanel implements Plotter<E> {

    private Chart chart;
    private Collection<? extends Date> yAxis;
    private final HashSet<Integer> instances = new HashSet<>(10);
    private XChartPanel chartPanel;
    //used for reverse search - finding which point belongs to which instance
    private LSH<E> lsh;

    public TimeXPlot() {
        initComponents(400, 400);
        // 2-D space
        lsh = new LSH(2, 10, 50, 6.0);
    }

    private void initComponents(int width, int height) {
        setLayout(new GridBagLayout());
        // Create Chart
        chart = new ChartBuilder().width(width).height(height).build();
        chart.getStyleManager().setLegendVisible(false);

        chart.getStyleManager().setXAxisLabelRotation(60);
        chart.getStyleManager().setDatePattern("MM-dd HH:mm");

        chartPanel = new XChartPanel(chart);
        PlotMouseListener ml = new PlotMouseListener(chart, this);
        chartPanel.addMouseListener(ml);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new java.awt.Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(chartPanel, c);
    }

    @Override
    public Insets getInsets() {
        return chartPanel.getInsets();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        chartPanel.setPreferredSize(preferredSize);
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        super.setPreferredSize(minimumSize);
        chartPanel.setMinimumSize(minimumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return chartPanel.getMinimumSize();
    }

    @Override
    public boolean isSupported(DataType type) {
        return type == DataType.TIMESERIES;
    }

    @Override
    public void prepare(DataType type) {
        //
    }

    @Override
    public void addInstance(E instance) {
        String name = instance.getIndex() + " " + instance.getName();
        addInstance(instance, name);
    }

    @Override
    public void addInstance(E instance, String clusterName) {
        ContinuousInstance inst = (ContinuousInstance) instance;
        Timeseries dataset = (Timeseries) inst.getParent();
        if (yAxis == null) {
            yAxis = dataset.getTimePointsCollection();
        }
        //make sure we don't add same data twice
        if (!instances.contains(instance.getIndex())) {
            StringBuilder sb = new StringBuilder();
            sb.append(inst.getIndex()).append(" - ").append(clusterName);
            Series s = chart.addSeries(sb.toString(), yAxis, new InstCollection(instance));
            instances.add(instance.getIndex());
            //kd-tree update
            updateTree(instance);
        }
    }

    private void updateTree(E instance) {
        int i = 0;
        for (Date d : yAxis) {
            //2D coordinates
            double[] key = new double[2];
            key[0] = d.getTime();
            key[1] = instance.get(i++);
            lsh.put(key, instance);
        }
    }

    @Override
    public void clearAll() {
        instances.clear();
    }

    @Override
    public void setTitle(String title) {
        chart.setChartTitle(title);
    }

    @Override
    public void setXBounds(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setYBounds(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E[] instanceAt(double[] coord, int maxK) {
        List<Neighbor<double[], E>> neighbors = new LinkedList<>();
        lsh.range(coord, maxK, neighbors);

        int size = maxK < neighbors.size() ? maxK : neighbors.size();
        E[] ret = (E[]) new Instance[size];
        for (int i = 0; i < size; i++) {
            ret[i] = neighbors.get(i).value;
        }

        return ret;
    }

    @Override
    public void focus(E instance) {
        if (instance != null) {
            this.setToolTipText(instance.getName());
        }
    }

}
