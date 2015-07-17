/*
 * Copyright (C) 2011-2015 clueminer.org
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
package org.clueminer.graph.adjacencyMatrix;

import java.util.ArrayList;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.graph.api.Edge;
import org.clueminer.graph.api.Graph;
import org.clueminer.graph.api.GraphFactory;
import org.clueminer.graph.api.Node;

/**
 *
 * @author tomas
 */
public class AdjMatrixFactory implements GraphFactory {

    private static AdjMatrixFactory instance;

    private static long nodeIdCounter;
    private static long edgeIdCounter;

    public static AdjMatrixFactory getInstance() {
        if (instance == null) {
            instance = new AdjMatrixFactory();
        }
        return instance;
    }

    protected AdjMatrixFactory() {
        nodeIdCounter = edgeIdCounter = 0;
    }

    @Override
    public Edge newEdge(Node source, Node target) {
        return new AdjMatrixEdge(edgeIdCounter++, source, target, 1);
    }

    @Override
    public Edge newEdge(Node source, Node target, boolean directed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Edge newEdge(Node source, Node target, int type, boolean directed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Edge newEdge(Node source, Node target, int type, double weight, boolean directed) {
        return new AdjMatrixEdge(edgeIdCounter++, source, target, weight);
    }

    @Override
    public Edge newEdge(Object id, Node source, Node target, int type, double weight, boolean directed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node newNode() {
        return new AdjMatrixNode(nodeIdCounter++);
    }

    @Override
    public Node newNode(Object label) {
        return new AdjMatrixNode(nodeIdCounter++, label);
    }

    @Override
    public Node newNode(Instance i) {
        return new AdjMatrixNode(nodeIdCounter++, i);
    }

    @Override
    public ArrayList<Node> createNodesFromInput(Dataset<? extends Instance> input) {
        ArrayList<Node> nodes = new ArrayList<>(input.size());
        for (Instance ins : input) {
            nodes.add(newNode(ins));
        }
        return nodes;
    }

    /**
     * {@inheritDoc }
     *
     * @param input
     * @param graph
     */
    @Override
    public Long[] createNodesFromInput(Dataset<? extends Instance> input, Graph graph) {
        Long[] mapping = new Long[input.size()];
        Node curr;
        for (Instance inst : input) {
            curr = newNode(inst);
            graph.addNode(curr);
            mapping[inst.getIndex()] = curr.getId();
        }
        return mapping;
    }

}
