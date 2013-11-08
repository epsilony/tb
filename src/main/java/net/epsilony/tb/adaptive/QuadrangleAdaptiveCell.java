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
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.WingedEdge;
import net.epsilony.tb.solid.winged.WingedUtils;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCell extends AbstractAdaptiveCell {

    @Override
    protected void genChildren(ArrayList<AdaptiveCellEdge> midEdges) {
        final int sideNum = getNumberOfVertes();
        children = new ArrayList<>(sideNum);
        Node centerNode = genCenterNode(midEdges);
        for (int side = 0; side < midEdges.size(); side++) {
            QuadrangleAdaptiveCell newChild = new QuadrangleAdaptiveCell();
            children.add(newChild);
            newChild.setVertexEdge(side, getVertexEdge(side));

            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            newEdge.setStart(midEdges.get(side).getEnd());
            newChild.setVertexEdge((side + 1) % sideNum, newEdge);

            newEdge = new AdaptiveCellEdge();
            newEdge.setStart(centerNode);
            newChild.setVertexEdge((side + 2) % sideNum, newEdge);
            newChild.setVertexEdge((side + 3) % sideNum, (WingedEdge) midEdges.get((side + 3) % sideNum).getSucc());
        }

        for (int side = 0; side < midEdges.size(); side++) {
            AdaptiveCell child = children.get(side);

            Segment2DUtils.link(midEdges.get(side), child.getVertexEdge((side + 1) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 1) % sideNum), child.getVertexEdge((side + 2) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 2) % sideNum), child.getVertexEdge((side + 3) % sideNum));

            WingedUtils.linkAsOpposite(child.getVertexEdge((side + 1) % sideNum), children.get((side + 1) % sideNum)
                    .getVertexEdge((side + 3) % sideNum));
        }

        for (int i = 0; i < children.size(); i++) {
            WingedUtils.linkEdgeAndCell(children.get(i));
            children.get(i).setLevel(level + 1);
        }
    }

    private Node genCenterNode(ArrayList<AdaptiveCellEdge> midEdges) {
        Node newNode = Node.instanceByClass(midEdges.get(0).getStart());
        newNode.setCoord(Math2D.intersectionPoint(midEdges.get(0).getEndCoord(), midEdges.get(2).getEndCoord(),
                midEdges.get(1).getEndCoord(), midEdges.get(3).getEndCoord(), null));
        return newNode;
    }

    @Override
    public int getNumberOfVertes() {
        return 4;
    }
}
