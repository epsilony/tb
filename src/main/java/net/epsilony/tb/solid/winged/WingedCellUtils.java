/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedCellUtils {

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
}
