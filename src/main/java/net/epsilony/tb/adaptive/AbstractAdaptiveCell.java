/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.Iterator;
import net.epsilony.tb.Math2D;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractAdaptiveCell implements AdaptiveCell {

    public static int defaultMaxSideSizePower = 1;
    AdaptiveCell[] children;
    AdaptiveCellEdge[] cornerEdges;
    int level = 0;
    int maxSideSizePower = defaultMaxSideSizePower;

    protected void bisectEdges() {
        for (int i = 0; i < cornerEdges.length; i++) {
            if (cornerEdges[i].getSucc() == cornerEdges[(i + 1) % getSideNum()]) {
                cornerEdges[i].bisect();
            }
        }
    }

    @Override
    public void fission() {
        if (!isAbleToFission()) {
            throw new IllegalStateException("Cannot fission!" + this);
        }
        bisectEdges();
        AdaptiveCellEdge[] midEdges = getEdgesEndBySideMids();
        genChildren(midEdges);
        cornerEdges = null;
    }

    protected abstract void genChildren(AdaptiveCellEdge[] midEdges);

    @Override
    public AdaptiveCell[] getChildren() {
        return children;
    }

    @Override
    public AdaptiveCellEdge[] getCornerEdges() {
        return cornerEdges;
    }

    protected AdaptiveCellEdge[] getEdgesEndBySideMids() {
        AdaptiveCellEdge[] result = new AdaptiveCellEdge[getSideNum()];
        int maxSideSize = getMaxSideSize();
        for (int side = 0; side < cornerEdges.length; side++) {
            AdaptiveCellEdge edgeA = cornerEdges[side];
            AdaptiveCellEdge edgeB = cornerEdges[(side + 1) % getSideNum()];
            double[] sideStart = edgeA.getStart().getCoord();
            double[] sideEnd = edgeB.getStart().getCoord();
            double[] midCoord = Math2D.pointOnSegment(sideStart, sideEnd, 0.5, null);
            AdaptiveCellEdge edge = edgeA;
            double lenSq = Math2D.distanceSquare(edge.getStart().getCoord(), edge.getEnd().getCoord());
            int count = 0;
            while (edge != edgeB) {
                AdaptiveCellEdge succ = edge.getSucc();
                double lenSqSucc = Math2D.distanceSquare(succ.getStart().getCoord(), succ.getEnd().getCoord());
                if (Math2D.distanceSquare(midCoord, edge.getEnd().getCoord()) < 0.2 * Math.min(lenSq, lenSqSucc)) {
                    result[side] = edge;
                }
                edge = succ;
                lenSq = lenSqSucc;
                count++;
                if (count > maxSideSize) {
                    throw new IllegalStateException();
                }
            }
        }
        return result;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxSideSize() {
        return (int) Math.pow(2, maxSideSizePower);
    }

    @Override
    public int getMaxSideSizePower() {
        return maxSideSizePower;
    }

    @Override
    public boolean isAbleToFission() {
        return null == searchFissionObstrutor();
    }

    @Override
    public Iterator<AdaptiveCellEdge> iterator() {
        if (null == cornerEdges) {
            return new SegmentIterator<>(null);
        }
        return new SegmentIterator<>(cornerEdges[0]);
    }

    @Override
    public AdaptiveCell searchFissionObstrutor() {
        Iterator<AdaptiveCellEdge> iter = new SegmentIterator<>(cornerEdges[0]);
        AdaptiveCell result = null;
        while (iter.hasNext()) {
            AdaptiveCellEdge edge = iter.next();
            AdaptiveCellEdge opposite = edge.getOpposite();
            if (null == opposite) {
                continue;
            }
            AdaptiveCell oppositeCell = opposite.getCell();
            if (null == oppositeCell) {
                continue;
            }
            if (getLevel() - oppositeCell.getLevel() >= getMaxSideSizePower()) {
                return oppositeCell;
            }
        }
        return result;
    }

    @Override
    public void setCornerEdges(AdaptiveCellEdge[] cornerEdges) {
        this.cornerEdges = cornerEdges;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setMaxSideSizePower(int maxSizeSizePower) {
        this.maxSideSizePower = maxSizeSizePower;
    }
}
