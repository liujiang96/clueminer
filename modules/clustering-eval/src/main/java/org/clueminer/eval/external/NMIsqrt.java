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
package org.clueminer.eval.external;

import org.clueminer.clustering.api.ExternalEvaluator;
import org.openide.util.lookup.ServiceProvider;

/**
 * Normalized mutual information used by Strehl and Ghosh
 *
 * A. Strehl and J. Ghosh. Cluster ensembles - a knowledge reuse framework for
 * combining multiple partitions. Journal of Machine Learning Research,
 * 3:583–617, 2002.
 *
 * @author deric
 */
@ServiceProvider(service = ExternalEvaluator.class)
public class NMIsqrt extends NMIbase {

    private static final String NAME = "NMI-sqrt";
    private static final long serialVersionUID = 5298781790787789513L;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double countNMI(double mutualInformation, double ent1, double ent2) {
        return mutualInformation / Math.sqrt(ent1 * ent2);
    }

}
