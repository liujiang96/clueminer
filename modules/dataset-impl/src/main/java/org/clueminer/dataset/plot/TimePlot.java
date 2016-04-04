/*
 * Copyright (C) 2011-2016 clueminer.org
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
package org.clueminer.dataset.plot;

import java.awt.Color;
import java.awt.Font;
import org.clueminer.dataset.api.ContinuousInstance;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.PlotType;
import org.clueminer.dataset.api.Plotter;
import org.clueminer.dataset.api.Timeseries;
import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;

/**
 * Basic timeseries chart
 *
 * @author Tomas Barton
 * @param <E>
 */
public class TimePlot<E extends Instance> extends Plot2DPanel implements Plotter<E> {

    private static final long serialVersionUID = 9134124279294818651L;

    public TimePlot() {
        super();
    }

    @Override
    public void addInstance(E instance) {
        ContinuousInstance inst = (ContinuousInstance) instance;
        Timeseries dataset = (Timeseries) inst.getParent();
        this.addLinePlot(instance.getName(), instance.getColor(), dataset.getTimePointsArray(), instance.arrayCopy());
    }

    @Override
    public void clearAll() {
        this.removeAllPlots();
    }

    @Override
    public void setTitle(String title) {
        BaseLabel label = new BaseLabel(title, Color.BLACK, 0.5, 1.1);
        label.setFont(new Font("serif", Font.BOLD, 20));
        this.addPlotable(label);
    }

    @Override
    public void setXBounds(double min, double max) {
        this.setFixedBounds(0, min, max);
    }

    @Override
    public void setYBounds(double min, double max) {
        this.setFixedBounds(1, min, max);
    }

    @Override
    public void prepare(PlotType type) {
        if (!isSupported(type)) {
            throw new RuntimeException("plot type " + type.name() + " is not supported");
        }
    }

    @Override
    public boolean isSupported(PlotType type) {
        return type == PlotType.TIMESERIES;
    }
}
