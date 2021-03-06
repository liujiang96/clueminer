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
package org.clueminer.distance;

import org.clueminer.distance.api.Distance;
import org.clueminer.math.Vector;
import org.openide.util.lookup.ServiceProvider;

/**
 * Apache FastMath.pow generates too many Split objects on heap. Standard Java seems
 * to be performing better in this case.
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = Distance.class)
public class EuclideanDistance extends MinkowskiDistance {

    private static final String NAME = "Euclidean";
    private static final long serialVersionUID = 3142545695613722167L;
    private static EuclideanDistance instance;
    //whether compute SQRT(sum) or not
    private boolean sqrt = true;

    public EuclideanDistance() {
        this.power = 2;
    }

    public EuclideanDistance(boolean sqrt) {
        this.sqrt = sqrt;
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Euclidean distance is quite frequently used, there's no need to create
     * instances all over again
     *
     * @return
     */
    public static EuclideanDistance getInstance() {
        if (instance == null) {
            instance = new EuclideanDistance();
        }
        return instance;
    }

    @Override
    public double measure(Vector<Double> x, Vector<Double> y) {
        checkInput(x, y);
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            sum += Math.pow(y.get(i) - x.get(i), power);
        }
        if (sqrt) {
            return Math.sqrt(sum);
        }
        return sum;
    }

    public double sqdist(Vector<Double> x, Vector<Double> y) {
        checkInput(x, y);
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            //should be faster
            sum += Math.pow(y.get(i) - x.get(i), power);
        }
        return sum;
    }

    @Override
    public double measure(Vector<Double> x, Vector<Double> y, double[] weights) {
        checkInput(x, y);
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            sum += Math.pow(weights[i] * y.get(i) - weights[i] * x.get(i), power);
        }

        if (sqrt) {
            return Math.sqrt(sum);
        }
        return sum;
    }

    @Override
    public double measure(double[] x, double[] y) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(y[i] - x[i], power);
        }

        if (sqrt) {
            return Math.sqrt(sum);
        }
        return sum;
    }

    public boolean isSqrt() {
        return sqrt;
    }

    public void setSqrt(boolean sqrt) {
        this.sqrt = sqrt;
    }

}
