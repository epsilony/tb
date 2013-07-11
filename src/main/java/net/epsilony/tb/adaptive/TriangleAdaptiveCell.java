/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCell<ND extends Node> extends AbstractAdaptiveCell<ND> {

    @Override
    protected void genChildren(ArrayList<AdaptiveCellEdge<ND>> midEdges) {
        final int sideNum = getNumberOfVertes();
        final int childrenNum = sideNum + 1;
        children = new ArrayList<>(childrenNum);


        for (int side = 0; side < midEdges.size(); side++) {
            TriangleAdaptiveCell child = new TriangleAdaptiveCell();
            children.add(child);

            child.setVertexEdge(side, getVertexEdge(side));
            AdaptiveCellEdge<ND> newEdge = new AdaptiveCellEdge<>();
            newEdge.setStart(midEdges.get(side).getEnd());
            child.setVertexEdge((side + 1) % sideNum, newEdge);
            child.setVertexEdge((side + 2) % sideNum, midEdges.get((side + 2) % sideNum).getSucc());
        }

        for (int side = 0; side < midEdges.size(); side++) {
            AdaptiveCell<ND> child = children.get(side);
            Segment2DUtils.link(midEdges.get(side), child.getVertexEdge((side + 1) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 1) % sideNum), child.getVertexEdge((side + 2) % sideNum));
        }

        TriangleAdaptiveCell lastChild = new TriangleAdaptiveCell();
        children.add(lastChild);

        for (int side = 0; side < lastChild.getNumberOfVertes(); side++) {
            AdaptiveCellEdge<ND> newEdge = new AdaptiveCellEdge<>();
            lastChild.setVertexEdge(side, newEdge);
            AdaptiveCellEdge<ND> opposite = children.get(side).getVertexEdge((side + 1) % sideNum);
            newEdge.setStart(opposite.getEnd());
            AdaptiveUtils.linkAsOpposite(newEdge, opposite);
        }

        AdaptiveUtils.linkCornerEdges(lastChild);

        for (int i = 0; i < children.size(); i++) {
            AdaptiveUtils.linkEdgeAndCell(children.get(i));
            children.get(i).setLevel(level + 1);
        }
    }

    @Override
    public int getNumberOfVertes() {
        return 3;
    }

    public List<TriangleAdaptiveCell<ND>> getEdgesNeighbours() {
        List<TriangleAdaptiveCell<ND>> result = new LinkedList<>();
        for (AdaptiveCellEdge<ND> edge : this) {
            AdaptiveCellEdge<ND> opposite = edge.getOpposite();
            if (null == opposite) {
                continue;
            }
            result.add((TriangleAdaptiveCell) opposite.getCell());
        }
        return result;
    }

    public Set<TriangleAdaptiveCell<ND>> getNodesNeighbours() {
        List<TriangleAdaptiveCell<ND>> edgeNeighbours = getEdgesNeighbours();
        Set<TriangleAdaptiveCell<ND>> nodesNeighbours = new HashSet<>(20);
        nodesNeighbours.addAll(edgeNeighbours);
        for (TriangleAdaptiveCell neibour : edgeNeighbours) {
            nodesNeighbours.addAll(neibour.getEdgesNeighbours());
        }
        return nodesNeighbours;
    }
}
