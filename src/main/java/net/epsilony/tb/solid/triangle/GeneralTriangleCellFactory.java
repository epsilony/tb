/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.triangle;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class GeneralTriangleCellFactory<
        CELL extends WingedCell<CELL, EDGE, NODE>, //
        EDGE extends WingedEdge<CELL, EDGE, NODE>, //
        NODE extends Node> implements Factory<CELL> {

    Factory<? extends CELL> cellFactory;
    Factory<? extends EDGE> edgeFactory;
    Factory<? extends NODE> nodeFactory;
    boolean genVertes = true;

    public GeneralTriangleCellFactory(Factory<? extends CELL> cellFactory, Factory<? extends EDGE> edgeFactory, Factory<? extends NODE> nodeFactory) {
        this.cellFactory = cellFactory;
        this.edgeFactory = edgeFactory;
        this.nodeFactory = nodeFactory;
    }

    public GeneralTriangleCellFactory() {
    }

    @Override
    public CELL produce() {
        CELL result = cellFactory.produce();
        for (int i = 0; i < 3; i++) {
            result.setVertexEdge(i, edgeFactory.produce());
            if (genVertes) {
                result.setVertex(i, nodeFactory.produce());
            }
        }
        for (int i = 0; i < 3; i++) {
            Segment2DUtils.link(result.getVertexEdge(i), result.getVertexEdge((i + 1) % 3));
            result.getVertexEdge(i).setCell(result);
        }
        return result;
    }

    public boolean isGenVertes() {
        return genVertes;
    }

    public void setGenVertes(boolean genVertes) {
        this.genVertes = genVertes;
    }

    public Factory<? extends CELL> getCellFactory() {
        return cellFactory;
    }

    public void setCellFactory(Factory<? extends CELL> cellFactory) {
        this.cellFactory = cellFactory;
    }

    public Factory<? extends EDGE> getEdgeFactory() {
        return edgeFactory;
    }

    public void setEdgeFactory(Factory<? extends EDGE> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }

    public Factory<? extends NODE> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(Factory<? extends NODE> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
