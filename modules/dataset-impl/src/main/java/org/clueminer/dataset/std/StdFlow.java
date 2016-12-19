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
package org.clueminer.dataset.std;

import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.flow.api.AbsFlowNode;
import org.clueminer.flow.api.FlowNode;
import org.clueminer.flow.api.FlowPanel;
import org.clueminer.std.Scaler;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author deric
 */
@ServiceProvider(service = FlowNode.class)
public class StdFlow<E extends Instance> extends AbsFlowNode implements FlowNode {

    private static final Logger LOG = LoggerFactory.getLogger(StdFlow.class);
    public static final String NAME = "data normalization";
    private final Class[] inputs = new Class[]{Dataset.class};
    private final Class[] outputs = new Class[]{Dataset.class};
    private DataScaler ds;

    public StdFlow() {
        ds = new DataScaler();
        panel = new StdFlowUI();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object[] getInputs() {
        return inputs;
    }

    @Override
    public Object[] getOutputs() {
        return outputs;
    }

    @Override
    public Object[] execute(Object[] inputs, Props params) {
        checkInputs(inputs);
        Object[] ret = new Object[1];
        String method = params.get("std", Scaler.NONE);
        boolean logscale = params.getBoolean("log-scale", false);
        LOG.info("normalizing data {}, logscale: {}",
                method, logscale);
        Dataset<E> dataset = (Dataset<E>) inputs[0];
        Dataset<E> norm = ds.standartize(dataset, method, logscale);
        ret[0] = norm;
        return ret;
    }

    private void checkInputs(Object[] in) {
        if (in.length != inputs.length) {
            throw new RuntimeException("expected " + inputs.length + " input(s), got " + in.length);
        }
        //type check
        int i = 0;
        for (Object obj : in) {
            if (!inputs[i].isInstance(obj)) {
                throw new RuntimeException("expected " + inputs[i].toString() + " input(s), got " + obj.getClass().toString());
            }
            i++;
        }

    }

    @Override
    public FlowPanel getPanel() {
        return panel;
    }

}
