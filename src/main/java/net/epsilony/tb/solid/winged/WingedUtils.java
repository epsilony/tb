/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedUtils {

    public static <CELL extends WingedCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> //
            Set<CELL> getNodesNeighbours(CELL cell) {
        List<CELL> edgeNeighbours = getEdgesNeighbours(cell);
        Set<CELL> nodesNeighbours = new HashSet<>(20);
        nodesNeighbours.addAll(edgeNeighbours);
        for (CELL neibour : edgeNeighbours) {
            nodesNeighbours.addAll(getEdgesNeighbours(neibour));
        }
        return nodesNeighbours;
    }

    public static <CELL extends WingedCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node>//
            List<CELL> getEdgesNeighbours(CELL cell) {
        ArrayList<CELL> result = new ArrayList<>(cell.getNumberOfVertes());
        for (int i = 0; i < cell.getNumberOfVertes(); i++) {
            result.add(cell.getVertexEdge(i).getOpposite().getCell());
        }
        return result;
    }

    public static void linkEdgeAndCell(WingedCell cell) {
        WingedEdge vertexEdge = cell.getVertexEdge(0);
        SegmentIterator<WingedEdge> iter = new SegmentIterator<>(vertexEdge);
        while (iter.hasNext()) {
            iter.next().setCell(cell);
        }
    }

    public static void linkCornerEdges(WingedCell cell) {
        for (int i = 0; i < cell.getNumberOfVertes(); i++) {
            Segment2DUtils.link(cell.getVertexEdge(i), cell.getVertexEdge((i + 1) % cell.getNumberOfVertes()));
        }
    }

    public static <CELL extends WingedCell<CELL, EDGE, NODE>, EDGE extends WingedEdge<CELL, EDGE, NODE>, NODE extends Node> //
            void linkAsOpposite(EDGE edgeA, EDGE edgeB) {
        edgeA.setOpposite(edgeB);
        edgeB.setOpposite(edgeA);
    }
}
