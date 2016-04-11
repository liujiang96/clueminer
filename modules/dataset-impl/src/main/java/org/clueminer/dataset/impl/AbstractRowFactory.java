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
package org.clueminer.dataset.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clueminer.dataset.api.Attribute;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.InstanceBuilder;

/**
 * Common methods for all instance builders.
 *
 * @author deric
 * @param <E>
 */
public abstract class AbstractRowFactory<E extends Instance> implements InstanceBuilder<E> {

    protected final Dataset<E> dataset;
    public static final int DEFAULT_CAPACITY = 5;
    protected DecimalFormat decimalFormat;

    public AbstractRowFactory(Dataset<E> dataset) {
        this.dataset = dataset;
    }

    public AbstractRowFactory(Dataset<E> dataset, char decimalPointChar) {
        this.dataset = dataset;
        decimalFormat = setupFormat(decimalPointChar);
    }

    private DecimalFormat setupFormat(char decimalPointChar) {
        //some locales (e.g. French, Czech) have ',' as a decimal separator
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimalPointChar);
        //symbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(symbols);
        return df;
    }

    @Override
    public E create(double[] values) {
        E row = build(values);
        dataset.add(row);
        return row;
    }

    @Override
    public E create(double[] values, Object classValue) {
        E row = create(values);
        row.setClassValue(classValue);
        return row;
    }

    @Override
    public E create(double[] values, String classValue) {
        E row = build(values, classValue);
        dataset.add(row);
        return row;
    }

    @Override
    public E build(double[] values, String classValue) {
        E row = build(values);
        row.setClassValue(classValue);
        return row;
    }

    @Override
    public E build() {
        return build(DEFAULT_CAPACITY);
    }

    /**
     * Build and add Instance to Dataset
     *
     * @return
     */
    @Override
    public E create() {
        E row = build();
        dataset.add(row);
        return row;
    }

    @Override
    public E createCopyOf(E orig, Dataset<E> parent) {
        E copy = createCopyOf(orig);
        copy.setParent(parent);
        return copy;
    }

    /**
     * Creates a new DataRow with the given initial capacity.
     *
     * @param size
     */
    @Override
    public E create(int size) {
        E row = build(size);
        dataset.add(row);
        return row;
    }

    @Override
    public E create(String[] values, Object classValue) {
        Attribute[] attr = (Attribute[]) dataset.getAttributes().values().toArray(new Attribute[dataset.attributeCount()]);
        E inst = create(values, attr);
        inst.setClassValue(classValue);
        return inst;
    }

    public static double string2Double(String str, DecimalFormat df) {
        if (str == null) {
            return Double.NaN;
        }
        try {
            //default English numbers
            if (df == null) {
                return Double.parseDouble(str);
            } else {
                Number num = df.parse(str);
                return num.doubleValue();
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(DoubleArrayFactory.class.getName())
                    .log(Level.SEVERE, "string2Double(String): ''{0}'' is not a valid number!", str);
            //TODO allow supressing exceptions by a parameter
            throw new RuntimeException("AbstractRowFactory.string2Double(String): " + str + " is not a valid number!");
            //return Double.NaN;
        } catch (ParseException ex) {
            Logger.getLogger(DoubleArrayFactory.class.getName())
                    .log(Level.SEVERE, "string2Double(String): ''{0}'' is not a valid number!", str);
            throw new RuntimeException("AbstractRowFactory.string2Double(String): " + str + " is not a valid number!");
        }
    }

}