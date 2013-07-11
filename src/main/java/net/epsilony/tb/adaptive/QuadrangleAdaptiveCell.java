/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCell<ND extends Node> extends AbstractAdaptiveCell<ND> {

    public static Logger logger = LoggerFactory.getLogger(QuadrangleAdaptiveCell.class);

    @Override
    protected void genChildren(ArrayList<AdaptiveCellEdge<ND>> midEdges) {
        final int sideNum = getNumberOfVertes();
        children = new ArrayList<>(sideNum);
        ND centerNode = genCenterNode(midEdges);
        for (int side = 0; side < midEdges.size(); side++) {
            QuadrangleAdaptiveCell<ND> newChild = new QuadrangleAdaptiveCell<>();
            children.add(newChild);
            newChild.setVertexEdge(side, getVertexEdge(side));

            AdaptiveCellEdge<ND> newEdge = new AdaptiveCellEdge<>();
            newEdge.setStart(midEdges.get(side).getEnd());
            newChild.setVertexEdge((side + 1) % sideNum, newEdge);

            newEdge = new AdaptiveCellEdge<>();
            newEdge.setStart(centerNode);
            newChild.setVertexEdge((side + 2) % sideNum, newEdge);
            newChild.setVertexEdge((side + 3) % sideNum, midEdges.get((side + 3) % sideNum).getSucc());
        }

        for (int side = 0; side < midEdges.size(); side++) {
            AdaptiveCell<ND> child = children.get(side);

            Segment2DUtils.link(midEdges.get(side), child.getVertexEdge((side + 1) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 1) % sideNum), child.getVertexEdge((side + 2) % sideNum));
            Segment2DUtils.link(child.getVertexEdge((side + 2) % sideNum), child.getVertexEdge((side + 3) % sideNum));

            AdaptiveUtils.linkAsOpposite(
                    child.getVertexEdge((side + 1) % sideNum),
                    children.get((side + 1) % sideNum).getVertexEdge((side + 3) % sideNum));
        }

        for (int i = 0; i < children.size(); i++) {
            AdaptiveUtils.linkEdgeAndCell(children.get(i));
            children.get(i).setLevel(level + 1);
        }
    }

    private ND genCenterNode(ArrayList<AdaptiveCellEdge<ND>> midEdges) {
        ND newNode = null;
        try {
            newNode = (ND) midEdges.get(0).getStart().getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("ND does not have a null constructor", ex);
            throw new IllegalStateException(ex);
        }
        newNode.setCoord(Math2D.intersectionPoint(
                midEdges.get(0).getEndCoord(), midEdges.get(2).getEndCoord(),
                midEdges.get(1).getEndCoord(), midEdges.get(3).getEndCoord(), null));
        return newNode;
    }

    @Override
    public int getNumberOfVertes() {
        return 4;
    }
}
