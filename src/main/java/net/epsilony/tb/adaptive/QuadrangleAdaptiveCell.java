/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCell extends AbstractAdaptiveCell {

    @Override
    protected void genChildren(AdaptiveCellEdge[] midEdges) {
        final int sideNum = getSideNum();
        children = new QuadrangleAdaptiveCell[sideNum];
        Node centerNode = genCenterNode(midEdges);
        for (int side = 0; side < midEdges.length; side++) {
            QuadrangleAdaptiveCell newChild = new QuadrangleAdaptiveCell();
            children[side] = newChild;
            AdaptiveCellEdge[] childCornerEdges = new AdaptiveCellEdge[sideNum];
            newChild.setCornerEdges(childCornerEdges);

            childCornerEdges[side] = cornerEdges[side];

            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            newEdge.setStart(midEdges[side].getEnd());
            childCornerEdges[(side + 1) % sideNum] = newEdge;

            newEdge = new AdaptiveCellEdge();
            newEdge.setStart(centerNode);
            childCornerEdges[(side + 2) % sideNum] = newEdge;

            childCornerEdges[(side + 3) % sideNum] = midEdges[(side + 3) % sideNum].getSucc();
        }

        for (int side = 0; side < midEdges.length; side++) {
            AdaptiveCell child = children[side];
            AdaptiveCellEdge[] childCornerEdges = child.getCornerEdges();

            Segment2DUtils.link(midEdges[side], childCornerEdges[(side + 1) % sideNum]);
            Segment2DUtils.link(childCornerEdges[(side + 1) % sideNum], childCornerEdges[(side + 2) % sideNum]);
            Segment2DUtils.link(childCornerEdges[(side + 2) % sideNum], childCornerEdges[(side + 3) % sideNum]);
        
            AdaptiveUtils.linkAsOpposite(
                    childCornerEdges[(side+1)%sideNum], 
                    children[(side+1)%sideNum].getCornerEdges()[(side+3)%sideNum]);
        }

        for (int i = 0; i < children.length; i++) {
            AdaptiveUtils.linkEdgeAndCell(children[i]);
            children[i].setLevel(level + 1);
        }
    }

    @Override
    public int getSideNum() {
        return 4;
    }

    private Node genCenterNode(AdaptiveCellEdge[] midEdges) {
        return new Node(Math2D.intersectionPoint(
                midEdges[0].getEndCoord(), midEdges[2].getEndCoord(),
                midEdges[1].getEndCoord(), midEdges[3].getEndCoord(), null));
    }
}
