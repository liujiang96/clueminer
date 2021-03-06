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
package org.clueminer.eval;

import org.clueminer.clustering.api.ScoreException;
import org.clueminer.fixtures.clustering.FakeClustering;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author deric
 */
public class TraceWTest {

    private final TraceW subject;
    private static final double DELTA = 1e-9;

    public TraceWTest() {
        subject = new TraceW();
    }

    @Test
    public void testIris() throws ScoreException {
        double scoreBetter = subject.score(FakeClustering.iris());
        double scoreWorser = subject.score(FakeClustering.irisWrong4());

        //should recognize better clustering
        assertEquals(true, subject.isBetter(scoreBetter, scoreWorser));

        // value according to R's NbClust package
        assertEquals(89.2974000000007, scoreBetter, DELTA);
    }

    /**
     * Check against definition (and tests in R package clusterCrit)
     * https://cran.r-project.org/web/packages/clusterCrit/index.html
     *
     * NOTE: There's a small problem with precision of floating point
     * operations. First 7 decimal digits seems to match.
     */
    @Test
    public void testClusterCrit() throws ScoreException {
        double score = subject.score(FakeClustering.int100p4());
        //clustCrit: 105.942129943902
        assertEquals(105.942129943902, score, DELTA);
    }

    @Test
    public void testSpeed() throws ScoreException {
        long ts = System.currentTimeMillis();
        double sc1 = subject.score(FakeClustering.wineClustering());
        long tot = System.currentTimeMillis() - ts;
        System.out.println("tracew(wine) took " + tot + " ms");
        assertEquals(5966.055555555556, sc1, DELTA);
    }

}
