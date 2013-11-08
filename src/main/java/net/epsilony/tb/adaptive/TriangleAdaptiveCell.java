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

package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.Triangle;
import net.epsilony.tb.solid.winged.WingedEdge;
import net.epsilony.tb.solid.winged.WingedUtils;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCell extends AbstractAdaptiveCell implements Triangle<Node> {

    @Override
    protected void genChildren(ArrayList<AdaptiveCellEdge> midEdges) {
        final int sideNum = getNumberOfVertes();
        final int childrenNum = sideNum + 1;
        children = new ArrayList<>(childrenNum);

        for (int side = 0; side < midEdges.size(); side++) {
            TriangleAdaptiveCell child = new TriangleAdaptiveCell();
            children.add(child);

            child.setVertexEdge(side, getVertexEdge(side));
            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            newEdge.setStart(midEdges.get(side).getEnd());
            child.setVertexEdge((side + 1) % sideNum, newEdge);
            child.setVertexEdge((side + 2) % sideNum, (WingedEdge) midEdges.get((side + 2) % sideNum).getSucc());
        }

        for (int side = 0; side < midEdges.size(); side++) {
            AdaptiveCell child = children.get(side);
            Segment2DUtils.link(midEdges.get(side), child.getVertexEdge((side + 1) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 1) % sideNum), child.getVertexEdge((side + 2) % sideNum));
        }

        TriangleAdaptiveCell lastChild = new TriangleAdaptiveCell();
        children.add(lastChild);

        for (int side = 0; side < lastChild.getNumberOfVertes(); side++) {
            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            lastChild.setVertexEdge(side, newEdge);
            AdaptiveCellEdge opposite = (AdaptiveCellEdge) children.get(side).getVertexEdge((side + 1) % sideNum);
            newEdge.setStart(opposite.getEnd());
            WingedUtils.linkAsOpposite(newEdge, opposite);
        }

        WingedUtils.linkCornerEdges(lastChild);

        for (int i = 0; i < children.size(); i++) {
            WingedUtils.linkEdgeAndCell(children.get(i));
            children.get(i).setLevel(level + 1);
        }
    }

    @Override
    public int getNumberOfVertes() {
        return 3;
    }

    public List<TriangleAdaptiveCell> getEdgesNeighbours() {
        List<TriangleAdaptiveCell> result = new LinkedList<>();
        for (AdaptiveCellEdge edge : this) {
            AdaptiveCellEdge opposite = (AdaptiveCellEdge) edge.getOpposite();
            if (null == opposite) {
                continue;
            }
            result.add((TriangleAdaptiveCell) opposite.getCell());
        }
        return result;
    }

    public Set<TriangleAdaptiveCell> getNodesNeighbours() {
        List<TriangleAdaptiveCell> edgeNeighbours = getEdgesNeighbours();
        Set<TriangleAdaptiveCell> nodesNeighbours = new HashSet<>(20);
        nodesNeighbours.addAll(edgeNeighbours);
        for (TriangleAdaptiveCell neibour : edgeNeighbours) {
            nodesNeighbours.addAll(neibour.getEdgesNeighbours());
        }
        return nodesNeighbours;
    }
}
