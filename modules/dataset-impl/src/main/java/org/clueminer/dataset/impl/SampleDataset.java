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
package org.clueminer.dataset.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.clueminer.attributes.AttributeFactoryImpl;
import org.clueminer.dataset.api.Attribute;
import org.clueminer.dataset.api.AttributeBuilder;
import org.clueminer.dataset.api.DataType;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.InstanceBuilder;
import org.clueminer.dataset.api.Plotter;
import org.clueminer.dataset.api.PlotterFactory;
import org.clueminer.dataset.api.StatsNum;
import org.openide.util.lookup.ServiceProvider;

/**
 * Strongly typed dataset based on standard Java ArrayList requires specified
 * all attributes before adding instances. If you don't know exactly the number
 * of instances, this is probably a good compromise between speed and
 * flexibility.
 *
 * @author Tomas Barton
 * @param <E>
 */
@ServiceProvider(service = Dataset.class)
public class SampleDataset<E extends Instance> extends AbstractDataset<E> implements Dataset<E> {

    private static final long serialVersionUID = -6412010424414577127L;
    protected Map<Integer, Attribute> attributes = new HashMap<>();
    protected AttributeBuilder attributeBuilder;
    protected TreeSet<Object> classes = new TreeSet<>();
    private static final Logger logger = Logger.getLogger(SampleDataset.class.getName());
    private int attrCapacity = -1;
    private int lastAttr = 0;

    /**
     * Creates an empty data set with capacity of ten
     */
    public SampleDataset() {
        super(10);
    }

    public SampleDataset(int capacity, int numAttrs) {
        attributes = new HashMap<>(numAttrs);
        attrCapacity = numAttrs;
    }

    /**
     * Create new dataset and references parent as an original dataset
     *
     * @param parent
     */
    public SampleDataset(Dataset<E> parent) {
        super(10);
        setParent(parent);
    }

    /**
     * Create dataset with given capacity of instances
     *
     * @param capacity
     */
    public SampleDataset(int capacity) {
        super(capacity);
    }

    protected void check(Collection<? extends E> c) {
        for (E i : c) {
            check(i);
        }
    }

    /**
     * Checks number of attributes in added instance
     *
     * @param i Instance
     */
    protected void check(E i) {
        if (i.classValue() != null) {
            classes.add(i.classValue());
        }
        if (i.size() > attributes.size()) {
            System.out.println(i);
            throw new RuntimeException("instance contains attributes that are not "
                    + "defined in dataset! expected " + attributes.size() + " attributes, but got: " + i.size());
        }
    }

    @Override
    public boolean add(E e) {
        check(e);
        instanceAdded(e);
        if (e.getIndex() < 0) {
            e.setIndex(size());
        }
        return super.add(e);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param index
     * @param e
     */
    @Override
    public void add(int index, E e) {
        check(e);
        e.setIndex(index);
        super.add(index, e);
    }

    @Override
    public final boolean addAll(Collection<? extends E> c) {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(Dataset<? extends E> d) {
        for (E i : d) {
            add(i);
        }
        return true;
    }

    /**
     * Inserts all of the elements in the specified collection into this list,
     * starting at the specified position.
     *
     * @param index
     * @param c
     * @return
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        check(c);
        return super.addAll(index, c);
    }

    @Override
    public void clear() {
        classes.clear();
        super.clear();
    }

    @Override
    public E instance(int index) {
        if (hasIndex(index)) {
            return get(index);
        } else if (index == size()) {
            //doesn't make sense to create instance with 0 attributes
            int attrs = attributeCount() == 0 ? attrCapacity : attributeCount();
            E inst = (E) builder().create(attrs);
            return inst;
        }
        throw new ArrayIndexOutOfBoundsException("can't get instance at position: " + index);
    }

    @Override
    public E getRandom(Random rand) {
        int max = this.size();
        int min = 0;
        int i = min + (int) (rand.nextDouble() * ((max - min) + 1));
        return get(i);
    }

    @Override
    public SortedSet<Object> getClasses() {
        return classes;
    }

    @Override
    public int attributeCount() {
        return lastAttr;
    }

    /**
     * Get name of i-th attribute
     *
     * @param i
     * @return
     */
    @Override
    public Attribute getAttribute(int i) {
        return attributes.get(i);
    }

    /**
     * Set i-th attribute (column)
     *
     * @param i
     * @param attr
     */
    @Override
    public void setAttribute(int i, Attribute attr) {
        if (i > lastAttr) {
            lastAttr = i;
        }
        attr.setIndex(i);
        attr.setDataset(this);
        attributes.put(i, attr);
    }

    @Override
    public void setAttributes(Map<Integer, Attribute> attr) {
        this.attributes = attr;
        for (Attribute a : attr.values()) {
            a.setDataset(this);
        }
        lastAttr = attr.size();
    }

    @Override
    public void setAttributes(Attribute[] attributes) {
        int i = attributeCount();
        for (Attribute a : attributes) {
            a.setDataset(this);
            attributes[i++] = a;
        }
        lastAttr = i;
    }

    /**
     * @TODO make sure, a deep copy is returned
     *
     * @return
     */
    @Override
    public Attribute[] copyAttributes() {
        return attributes.values().toArray(new Attribute[attributeCount()]);
    }

    /**
     * Reference to attributes specification
     *
     * @return
     */
    @Override
    public Map<Integer, Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public int classIndex(Object clazz) {
        if (clazz != null) {
            return this.getClasses().headSet(clazz).size();
        } else {
            return -1;
        }

    }

    @Override
    public Object classValue(int index) {
        int i = 0;
        for (Object o : this.classes) {
            if (i == index) {
                return o;
            }
            i++;
        }
        return null;
    }

    @Override
    public void changedClass(Object orig, Object current, Object source) {
        if (current != null) {
            if (!classes.contains(current)) {
                classes.add(current);
            }
        }
    }

    /**
     * When an item is added, we have to recompute statistics
     *
     * @param row
     */
    private void instanceAdded(Instance row) {
        Attribute a;
        for (int i = 0; i < attributeCount(); i++) {
            a = attributes.get(i);
            a.updateStatistics(row.value(i));
        }
    }

    @Override
    public InstanceBuilder builder() {
        if (builder == null) {
            builder = new DoubleArrayFactory(this, '.');
        }
        return builder;
    }

    @Override
    public AttributeBuilder attributeBuilder() {
        if (attributeBuilder == null) {
            attributeBuilder = new AttributeFactoryImpl<>(this);
        }
        return attributeBuilder;
    }

    /**
     * Deep copy of dataset
     *
     * @return
     */
    @Override
    public Dataset<E> copy() {
        SampleDataset out = new SampleDataset();
        out.attributes = this.attributes;
        out.lastAttr = attributes.size();
        for (Instance i : this) {
            out.add(i.copy());
        }
        return out;
    }

    /**
     * @TODO consider using hashmap for attribute names. though this dataset is
     * not really meant for this type of operations
     *
     * @param attributeName
     * @param instanceIdx
     * @return
     */
    @Override
    public double getAttributeValue(String attributeName, int instanceIdx) {
        int i = 0;
        while (i < attributeCount()) {
            if (attributes.get(i).getName().equals(attributeName)) {
                return get(instanceIdx, i);
            }
            i++;
        }
        throw new RuntimeException("attribute " + attributeName + " not found");
    }

    @Override
    public double getAttributeValue(Attribute attribute, int instanceIdx) {
        return get(instanceIdx).value(attribute.getIndex());
    }

    /**
     * {@inheritDoc}
     *
     * @param instanceIdx
     * @param attributeIndex
     * @return
     */
    @Override
    public double get(int instanceIdx, int attributeIndex) {
        return get(instanceIdx).value(attributeIndex);
    }

    @Override
    public void setAttributeValue(String attributeName, int instanceIdx, double value) {
        int i = 0;
        boolean success = false;
        while (i < attributeCount() && !success) {
            if (attributes.get(i).getName().equals(attributeName)) {
                instance(instanceIdx).set(i, value);
                success = true;
            }
            i++;
        }
        if (!success) {
            throw new RuntimeException("attribute " + attributeName + " not found");
        }
    }

    @Override
    public JComponent getPlotter() {
        PlotterFactory factory = PlotterFactory.getInstance();
        for (Plotter p : factory.getAll()) {
            if (p.isSupported(DataType.DISCRETE)) {
                for (E inst : this) {
                    p.addInstance(inst);
                }
                return (JComponent) p;
            }
        }
        throw new RuntimeException("No visualization found for data type " + this.getClass().getName());
    }

    @Override
    public Attribute getAttribute(String attributeName) {
        for (Attribute a : attributes.values()) {
            if (a.getName().equals(attributeName)) {
                return a;
            }
        }
        throw new RuntimeException("attribute " + attributeName + " was not found");
    }

    @Override
    public Dataset<E> duplicate() {
        SampleDataset<E> copy = new SampleDataset<>(this.size());
        copy.setAttributes(attributes);
        copy.setClasses(this.getClasses());
        return copy;
    }

    @Override
    public void addAttribute(Attribute attr) {
        attr.setDataset(this);
        attr.setIndex(lastAttr);
        attributes.put(lastAttr++, attr);
    }

    @Override
    public Attribute removeAttribute(int index) {
        Attribute attr = attributes.get(index);
        lastAttr--;
        return attr;
    }

    @Override
    public double min() {
        double min = Double.POSITIVE_INFINITY, curr;
        for (Attribute attribute : attributes.values()) {
            curr = attribute.statistics(StatsNum.MIN);
            if (curr < min) {
                min = curr;
            }
        }
        return min;
    }

    @Override
    public double max() {
        double max = Double.NEGATIVE_INFINITY, curr;
        for (Attribute attribute : attributes.values()) {
            curr = attribute.statistics(StatsNum.MAX);
            if (curr > max) {
                max = curr;
            }
        }
        return max;
    }

    @Override
    public void resetStats() {
        for (Attribute attribute : attributes.values()) {
            attribute.resetStats();
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SampleDataset(size= " + size() + ", attrSize= " + attributeCount() + ") [");
        Instance inst;
        for (int i = 0; i < size(); i++) {
            if (i > 0) {
                str.append(", ");
            }
            if (i % 3 == 0) {
                str.append("\n ");
            }
            inst = get(i);
            str.append("{").append(inst.getIndex()).append("}");
            str.append(inst.classValue()).append(": ").append(inst.toString());
        }
        str.append("\n ]");
        return str.toString();
    }

    @Override
    public Collection<? extends Number> attrCollection(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void setClasses(SortedSet<Object> klasses) {
        for (Object o : klasses) {
            if (!this.classes.contains(o)) {
                classes.add(o);
            }
        }
    }

}
