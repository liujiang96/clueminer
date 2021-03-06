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
package org.clueminer.graph.adjacencyList;

import org.clueminer.graph.api.EdgeType;
import org.clueminer.graph.api.Edge;
import org.clueminer.graph.api.Node;
import org.clueminer.graph.impl.ElemImpl;

/**
 *
 * @author Hamster
 */
public class AdjListEdge extends ElemImpl implements Edge {

    private final Node source;
    private final Node target;
    private double weight;
    private EdgeType direction;

    AdjListEdge(long id, Node source, Node target) {
        super(id);
        this.source = source;
        this.target = target;
        this.weight = 1.0;
        this.direction = EdgeType.NONE;
    }

    AdjListEdge(long id, Node source, Node target, boolean directed) {
        super(id);
        this.source = source;
        this.target = target;
        this.weight = 1.0;
        if (directed) {
            this.direction = EdgeType.FORWARD;
        } else {
            this.direction = EdgeType.NONE;
        }
    }

    AdjListEdge(long id, Node source, Node target, boolean directed, double weight) {
        super(id);
        this.source = source;
        this.target = target;
        this.weight = weight;
        if (directed) {
            this.direction = EdgeType.FORWARD;
        } else {
            this.direction = EdgeType.NONE;
        }
    }

    @Override
    public boolean isDirected() {
        return direction != EdgeType.NONE;
    }

    @Override
    public Node getSource() {
        return source;
    }

    @Override
    public Node getTarget() {
        return target;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public Object getLabel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public EdgeType getDirection() {
        return direction;
    }

    @Override
    public void setDirection(EdgeType direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "e" + id + ": n" + source.getId() + " -(" + weight + ")-> n" + target.getId();
    }

}
