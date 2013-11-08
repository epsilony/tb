/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
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
