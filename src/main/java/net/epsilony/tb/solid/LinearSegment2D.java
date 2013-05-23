/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LinearSegment2D extends AbstractSegment2D {

    public LinearSegment2D() {
    }

    public LinearSegment2D(Node start) {
        this.start = start;
    }

    public double length() {
        return Math2D.distance(getStartCoord(), getEndCoord());
    }

    @Override
    public LinearSegment2D bisectionAndReturnNewSuccessor() {
        LinearSegment2D newSucc = newInstance();
        newSucc.setStart(bisectionNode());
        newSucc.succ = this.succ;
        newSucc.pred = this;
        this.succ.setPred(newSucc);
        this.succ = newSucc;
        return newSucc;
    }

    protected Node bisectionNode() {
        return new Node(Segment2DUtils.chordMidPoint(this, null));
    }

    @Override
    public String toString() {
        String endStr = (null == succ || null == getEnd()) ? "NULL" : getEnd().toString();
        String startStr = (null == start) ? "NULL" : start.toString();
        return String.format("Segment2D(%d)[h:(%s), r:(%s)]", id, startStr, endStr);
    }

    protected LinearSegment2D newInstance() {
        return new LinearSegment2D();
    }

    @Override
    public double[] values(double t, double[] results) {
        if (null == results) {
            results = new double[diffOrder * 2];
        }
        double[] startCoord = getStartCoord();
        double[] endCoord = getEndCoord();
        Math2D.pointOnSegment(startCoord, endCoord, t, results);
        if (diffOrder >= 1) {
            results[2] = endCoord[0] - startCoord[0];
            results[3] = endCoord[1] - startCoord[1];
        }
        return results;
    }
}
