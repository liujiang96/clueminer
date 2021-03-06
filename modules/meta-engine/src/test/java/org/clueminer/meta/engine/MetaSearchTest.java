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
package org.clueminer.meta.engine;

import java.util.List;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.evolution.api.Individual;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.clueminer.report.MemInfo;
import org.clueminer.utils.Props;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author deric
 * @param <I>
 * @param <E>
 * @param <C>
 */
public class MetaSearchTest<I extends Individual<I, E, C>, E extends Instance, C extends Cluster<E>> {

    private final MetaSearch<I, E, C> subject;
    private MemInfo mem;

    public MetaSearchTest() {
        subject = new MetaSearch();
    }

    @Before
    public void setUp() {
        //report = new ConsoleReporter();
        //subject.addEvolutionListener(report);
        mem = new MemInfo();
    }

    @Test
    public void testIris() throws Exception {
        subject.setDataset((Dataset<E>) FakeDatasets.schoolData());
        subject.setGenerations(1);
        subject.setPopulationSize(15);
        subject.setMaxSolutions(20);

        mem.startClock();
        List<Clustering<E, C>> res = subject.call();
        assertNotNull(res);

        //TODO: findout why this fails
        //assertEquals(q.size(), ids.size());
        mem.report();
    }

    //@Test
    public void testVehicle() {
        subject.setDataset((Dataset<E>) FakeDatasets.vehicleDataset());
        subject.setGenerations(1);
        subject.setPopulationSize(5);

        mem.startClock();
        //TODO: make sure evolution works
        subject.run();
        mem.report();
    }

    @Test
    public void testConf() {
        Props p = new Props();
        p.put("objectives", "AIC,BIC");
        subject.configure(p);
        List<ClusterEvaluation<E, C>> ce = subject.getObjectives();
        assertEquals(2, ce.size());
        assertEquals("AIC", ce.get(0).getName());
    }
}
