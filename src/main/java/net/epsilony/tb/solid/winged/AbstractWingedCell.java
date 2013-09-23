/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractWingedCell implements WingedCell {

    ArrayList<WingedEdge> vertesEdges = new ArrayList<>(getNumberOfVertes());

    protected AbstractWingedCell() {
        for (int i = 0; i < getNumberOfVertes(); i++) {
            vertesEdges.add(null);
        }
    }

    @Override
    public WingedEdge getVertexEdge(int i) {
        return vertesEdges.get(i);
    }

    @Override
    public void setVertexEdge(int i, WingedEdge edge) {
        vertesEdges.set(i, edge);
    }

    @Override
    public void setVertex(int index, Node vertex) {
        getVertexEdge(index).setStart(vertex);
    }

    @Override
    public Node getVertex(int index) {
        return getVertexEdge(index).getStart();
    }
}
