/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedUtils {

    public static Set<WingedCell> getNodesNeighbours(WingedCell cell) {
        List<WingedCell> edgeNeighbours = getEdgesNeighbours(cell);
        Set<WingedCell> nodesNeighbours = new HashSet<>(20);
        nodesNeighbours.addAll(edgeNeighbours);
        for (WingedCell neibour : edgeNeighbours) {
            nodesNeighbours.addAll(getEdgesNeighbours(neibour));
        }
        return nodesNeighbours;
    }

    public static List<WingedCell> getEdgesNeighbours(WingedCell cell) {
        ArrayList<WingedCell> result = new ArrayList<>(cell.getNumberOfVertes());
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

    public static void linkAsOpposite(WingedEdge edgeA, WingedEdge edgeB) {
        edgeA.setOpposite(edgeB);
        edgeB.setOpposite(edgeA);
    }
}
