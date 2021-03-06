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
package org.clueminer.dendrogram;

import java.util.Iterator;
import java.util.Map;
import org.clueminer.dataset.api.DataProvider;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;

/**
 *
 * @author Tomas Barton
 * @param <E>
 */
public class DataProviderMap<E extends Instance> implements DataProvider<E> {

    private final Map<String, Dataset<E>> data;

    public DataProviderMap(Map<String, Dataset<E>> data) {
        this.data = data;
    }

    @Override
    public String[] getDatasetNames() {
        return data.keySet().toArray(new String[data.size()]);
    }

    @Override
    public Dataset<E> getDataset(String name) {
        return data.get(name);
    }

    @Override
    public Dataset<E> first() {
        return data.values().iterator().next();
    }

    @Override
    public int count() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Iterator<Dataset<E>> iterator() {
        return new DataProviderMapIterator();
    }

    @Override
    public boolean hasDataset(String name) {
        for (String dataset : data.keySet()) {
            if (name.equals(dataset)) {
                return true;
            }
        }
        return false;
    }

    private class DataProviderMapIterator implements Iterator<Dataset<E>> {

        private int index;
        private final String[] names;

        public DataProviderMapIterator() {
            names = getDatasetNames();
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < count();
        }

        @Override
        public Dataset<E> next() {
            return data.get(names[index++]);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("not supported yet.");
        }

    }

}
