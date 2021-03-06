/*
 * Copyright (C) 2011-2018 clueminer.org
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
package org.clueminer.bio;

import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.fixtures.BioFixture;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author deric
 * @param <E>
 */
public class FastaParserTest<E extends Instance> {

    private final FastaParser subject;
    private static final BioFixture BF = new BioFixture();

    public FastaParserTest() {
        subject = new FastaParser();
    }

    @Test
    public void testLoad_File_Dataset() throws Exception {
        Dataset<E> dataset = new GenomeSet(15, 2);
        subject.load(BF.genomeFasta(), dataset);
        assertEquals(38, dataset.size());
    }

    @Test
    public void testLoad_Reader_Dataset() throws Exception {
    }

}
