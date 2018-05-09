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
package org.clueminer.ap;

import org.apache.commons.math3.util.FastMath;
import org.clueminer.clustering.api.Configurator;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.utils.Props;

/**
 *
 * @author deric
 */
public class APConfig<E extends Instance> implements Configurator<E> {

    private static APConfig instance;

    private APConfig() {

    }

    public static APConfig getInstance() {
        if (instance == null) {
            instance = new APConfig();
        }
        return instance;
    }

    @Override
    public void configure(Dataset<E> dataset, Props params) {
        //TODO
    }

    @Override
    public double estimateRunTime(Dataset<E> dataset, Props params) {
        return FastMath.pow(dataset.size(), 2) * dataset.attributeCount();
    }

}
