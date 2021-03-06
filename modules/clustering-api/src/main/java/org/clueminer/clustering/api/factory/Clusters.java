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
package org.clueminer.clustering.api.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.clueminer.clustering.api.Cluster;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author deric
 */
public class Clusters {

    public static Cluster newInst() {
        //simple lookup would return an existing instance on any clustering
        Class c = Lookup.getDefault().lookup(Cluster.class).getClass();
        Cluster res = null;
        try {
            res = (Cluster) c.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        return res;
    }

    public static Cluster newInst(int size) {
        Class c = Lookup.getDefault().lookup(Cluster.class).getClass();
        Cluster res = null;
        try {
            Constructor<?> ctor = c.getConstructor(Integer.class);
            res = (Cluster) ctor.newInstance(new Object[]{size});
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        return res;
    }

}
