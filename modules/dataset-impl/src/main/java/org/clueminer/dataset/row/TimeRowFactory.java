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
package org.clueminer.dataset.row;

import java.util.HashMap;
import java.util.Map;
import org.clueminer.attributes.BasicAttrType;
import org.clueminer.dataset.api.Attribute;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.InstanceBuilder;
import org.clueminer.dataset.api.TypeHandler;
import org.clueminer.dataset.impl.AbstractRowFactory;
import static org.clueminer.dataset.impl.AbstractRowFactory.string2Double;
import org.clueminer.exception.ParserError;

/**
 *
 * @author Tomas Barton
 * @param <E>
 */
public class TimeRowFactory<E extends TimeRow> extends AbstractRowFactory<E> implements InstanceBuilder<E> {

    private int capacity = 50;

    // Make a map that translates a Class object to a Handler
    private static final Map<Class, TypeHandler> dispatch = new HashMap<>();

    static {
        dispatch.put(Double.class, new TypeHandler() {
            @Override
            public void handle(Object value, Attribute attr, Instance row, InstanceBuilder builder) {
                row.set(attr.getIndex(), (Double) value);
            }
        });
        dispatch.put(Float.class, new TypeHandler() {
            @Override
            public void handle(Object value, Attribute attr, Instance row, InstanceBuilder builder) {
                row.set(attr.getIndex(), (Float) value);
            }
        });
        dispatch.put(Integer.class, new TypeHandler() {
            @Override
            public void handle(Object value, Attribute attr, Instance row, InstanceBuilder builder) {
                row.set(attr.getIndex(), (Integer) value);
            }
        });
        dispatch.put(Boolean.class, new TypeHandler() {
            @Override
            public void handle(Object value, Attribute attr, Instance row, InstanceBuilder builder) {
                row.set(attr.getIndex(), (boolean) value ? 1.0 : 0.0);
            }
        });
        dispatch.put(String.class, new TypeHandler() {
            @Override
            public void handle(Object value, Attribute attr, Instance row, InstanceBuilder builder) throws ParserError {
                BasicAttrType at = (BasicAttrType) attr.getType();
                switch (at) {
                    case NUMERICAL:
                    case NUMERIC:
                    case REAL:
                        row.set(attr.getIndex(), string2Double(value.toString(), builder.getDecimalFormat()));
                        break;
                    default:
                        throw new RuntimeException("conversion to " + at + " is not supported for '" + value + "'");
                }

            }
        });
    }

    public TimeRowFactory(Dataset<E> dataset) {
        super(dataset);
    }

    public TimeRowFactory(Dataset<E> dataset, int capacity) {
        super(dataset);
        if (capacity > 0) {
            this.capacity = capacity;
        }
    }

    @Override
    public TimeRow createCopyOf(TimeRow orig) {
        TimeRow copy = new TimeRow(Double.class, orig.getCapacity());
        copy.timePoints = orig.timePoints;
        return copy;
    }

    @Override
    public E create(int capacity) {
        TimeRow inst = build(capacity);
        dataset.add((E) inst);
        return (E) inst;
    }

    @Override
    public E build(int capacity) {
        return (E) new TimeRow(Double.class, capacity);
    }

    @Override
    public E create(double[] values) {
        TimeRow inst = build(values);
        dataset.add((E) inst);
        return (E) inst;
    }

    @Override
    public E build(double[] values) {
        int numAttr = dataset.attributeCount();
        if (values.length != numAttr) {
            throw new RuntimeException("expected " + numAttr + "attributes but got " + values.length);
        }
        TimeRow inst = new TimeRow(Double.class, capacity);
        for (int i = 0; i < values.length; i++) {
            inst.set(i, values[i]);
        }
        return (E) inst;
    }

    @Override
    public E create(double[] values, Object classValue) {
        E inst = build(values, (String) classValue);
        dataset.add(inst);
        return inst;
    }

    @Override
    public E create(double[] values, String classValue) {
        TimeRow inst = create(values);
        inst.setClassValue(classValue);
        return (E) inst;
    }

    @Override
    public E create(String[] strings, Attribute[] attributes) {
        double val[] = new double[strings.length];
        int i = 0;
        for (String str : strings) {
            val[i++] = Double.valueOf(str);
        }
        return (E) create(val);
    }

    @Override
    public E build(double[] values, String classValue) {
        TimeRow inst = create(values);
        inst.setClassValue(classValue);
        return (E) inst;
    }

    @Override
    protected void dispatch(Object value, Attribute attr, E row) throws ParserError {
        TypeHandler h = dispatch.get(value.getClass());
        if (h == null) {
            // Throw an exception: unknown type
            throw new RuntimeException("could not convert " + value.getClass().getName() + " to " + attr.getType());
        }
        h.handle(value, attr, row, this);
    }
}
