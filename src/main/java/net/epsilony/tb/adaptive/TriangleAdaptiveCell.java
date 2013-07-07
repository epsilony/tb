/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCell extends AbstractAdaptiveCell {

    @Override
    protected void genChildren(AdaptiveCellEdge[] midEdges) {
        final int sideNum = getSideNum();
        children = new TriangleAdaptiveCell[sideNum + 1];


        for (int side = 0; side < midEdges.length; side++) {
            TriangleAdaptiveCell newChild = new TriangleAdaptiveCell();
            children[side] = newChild;
            AdaptiveCellEdge[] childEdges = new AdaptiveCellEdge[sideNum];
            newChild.setCornerEdges(childEdges);
            childEdges[side] = cornerEdges[side];
            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            newEdge.setStart(midEdges[side].getEnd());
            childEdges[(side + 1) % sideNum] = newEdge;
            childEdges[(side + 2) % sideNum] = midEdges[(side + 2) % sideNum].getSucc();
        }

        for (int side = 0; side < midEdges.length; side++) {
            AdaptiveCell child = children[side];
            Segment2DUtils.link(midEdges[side], child.getCornerEdges()[(side + 1) % sideNum]);
            Segment2DUtils.link(child.getCornerEdges()[(side + 1) % sideNum], child.getCornerEdges()[(side + 2) % sideNum]);
        }

        TriangleAdaptiveCell lastChild = new TriangleAdaptiveCell();
        children[sideNum] = lastChild;
        AdaptiveCellEdge[] lastChildEdges = new AdaptiveCellEdge[sideNum];
        lastChild.setCornerEdges(lastChildEdges);
        for (int side = 0; side < lastChildEdges.length; side++) {
            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            lastChildEdges[side] = newEdge;
            AdaptiveCellEdge opposite = children[side].getCornerEdges()[(side + 1) % sideNum];
            newEdge.setStart(opposite.getEnd());
            AdaptiveUtils.linkAsOpposite(newEdge, opposite);
        }

        AdaptiveUtils.linkCornerEdges(lastChild);

        for (int i = 0; i < children.length; i++) {
            AdaptiveUtils.linkEdgeAndCell(children[i]);
            children[i].setLevel(level + 1);
        }
    }

    @Override
    public int getSideNum() {
        return 3;
    }

    public List<TriangleAdaptiveCell> getEdgesNeighbours() {
        List<TriangleAdaptiveCell> result = new LinkedList<>();
        for (AdaptiveCellEdge edge : this) {
            AdaptiveCellEdge opposite = edge.getOpposite();
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
