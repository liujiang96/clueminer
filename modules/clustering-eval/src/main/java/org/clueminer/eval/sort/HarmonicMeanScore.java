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
package org.clueminer.eval.sort;

import java.util.Arrays;
import java.util.List;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.Rank;
import org.clueminer.clustering.api.config.ConfigException;
import org.clueminer.dataset.api.Instance;
import org.openide.util.lookup.ServiceProvider;

/**
 * Harmonic Mean score ranking strategy
 *
 * Based on: Vendramin, L.; Jaskowiak, P.A.; Campello, R.J. On the combination
 * of relative clustering validity criteria. In Proceedings of the 25th
 * International Conference on Scientific and Statistical Database Management,
 * ACM, 2013, pp. 4–15.
 *
 * @author deric
 * @param <E>
 * @param <C>
 */
@ServiceProvider(service = Rank.class)
public class HarmonicMeanScore<E extends Instance, C extends Cluster<E>> implements Rank<E, C> {

    private static final String NAME = "Harmonic Mean";
    private HarmonicMeanComparator comp = new HarmonicMeanComparator();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Clustering<E, C>[] sort(Clustering<E, C>[] clusterings, List<ClusterEvaluation<E, C>> objectives) {
        comp.setObjectives(objectives);
        //scan input values
        comp.updateStats(clusterings);

        //sort
        Arrays.parallelSort(clusterings, comp);

        return clusterings;
    }

    @Override
    public boolean isMultiObjective() {
        return false;
    }

    @Override
    public HarmonicMeanComparator getComparator() {
        return comp;
    }

    @Override
    public ClusterEvaluation<E, C> getEvaluator() {
        return comp;
    }

    @Override
    public void validate(List<ClusterEvaluation<E, C>> objectives) throws ConfigException {
        if (objectives.size() < 2) {
            throw new ConfigException("Please provide at least two evaluation metrics. " + objectives.size() + " was given");
        }
    }

    @Override
    public int getMinObjectives() {
        return 2;
    }

}
