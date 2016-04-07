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
package org.clueminer.knn;

import java.util.LinkedList;
import java.util.List;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.neighbor.Neighbor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author deric
 */
public class LinearRNNTest extends KnnTest {

    private final LinearRNN subject;

    public LinearRNNTest() {
        subject = new LinearRNN();
    }

    @Test
    public void testRange() {
        Dataset<? extends Instance> d = irisDataset();
        subject.setDataset(d);

        List<Neighbor<Instance>> neighbors = new LinkedList<>();
        Instance ref = d.get(9);
        double range = 0.029;
        subject.range(ref, range, neighbors);
        for (Neighbor<Instance> neighbor : neighbors) {
            assertEquals(0.0, neighbor.distance, DELTA);
        }
        //there are 2 same instances
        assertEquals(2, neighbors.size());
    }

}
