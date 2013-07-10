/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractWingedCell<CELL extends AbstractWingedCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> implements WingedCell<CELL, EDGE, ND> {

    ArrayList<EDGE> vertesEdges = new ArrayList<>(getNumberOfVertes());

    protected AbstractWingedCell() {
        for (int i = 0; i < getNumberOfVertes(); i++) {
            vertesEdges.add(null);
        }
    }

    @Override
    public EDGE getVertexEdge(int i) {
        return vertesEdges.get(i);
    }

    @Override
    public void setVertexEdge(int i, EDGE edge) {
        vertesEdges.set(i, edge);
    }

    @Override
    public void setVertex(int index, ND vertex) {
        getVertexEdge(index).setStart(vertex);
    }

    @Override
    public ND getVertex(int index) {
        return getVertexEdge(index).getStart();
    }
}
