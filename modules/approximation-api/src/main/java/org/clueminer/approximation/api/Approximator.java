/*
 * Copyright (C) 2011-2017 clueminer.org
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
package org.clueminer.approximation.api;

import java.util.HashMap;
import org.clueminer.dataset.api.ContinuousInstance;

/**
 * Compute characteristic number(s) of given dataset
 *
 * @author Tomas Barton
 */
public abstract class Approximator {

    public abstract String getName();

    public abstract void estimate(double[] xAxis, ContinuousInstance instance, HashMap<String, Double> coefficients);

    /**
     * Names of all attributes provided by this approximator
     *
     * @return array of Strings (names)
     */
    public abstract String[] getParamNames();

    public abstract double getFunctionValue(double x, double[] coeff);

    public double[] parseParams(HashMap<String, Double> coefficients) {
        String[] names = getParamNames();
        double[] res = new double[names.length];
        for (int i = 0; i < names.length; i++) {
            res[i] = coefficients.get(names[i]);
        }
        return res;
    }

    /**
     *
     * @return number of coefficients generated by this approximator
     */
    public abstract int getNumCoefficients();
}
