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

    @Override
    public double distanceTo(double x, double y) {
        double[] v1 = getStart().coord;
        double[] v2 = getEnd().coord;
        double d12_x = v2[0] - v1[0];
        double d12_y = v2[1] - v1[1];
        double len12 = Math.sqrt(d12_x * d12_x + d12_y * d12_y);
        double d1p_x = x - v1[0];
        double d1p_y = y - v1[1];
        double project_len = Math2D.dot(d1p_x, d1p_y, d12_x, d12_y) / len12;
        if (project_len > len12) {
            double dx = x - v2[0];
            double dy = y - v2[1];
            return Math.sqrt(dx * dx + dy * dy);
        } else if (project_len < 0) {
            return Math.sqrt(d1p_x * d1p_x + d1p_y * d1p_y);
        } else {
            return Math.abs(Math2D.cross(d12_x, d12_y, d1p_x, d1p_y)) / len12;
        }
    }

    public double length() {
        return Math2D.distance(getStartCoord(), getEndCoord());
    }

    public double[] midPoint(double[] result) {
        double[] start = getStart().coord;
        double[] end = getEnd().coord;
        double x = (start[0] + end[0]) / 2;
        double y = (start[1] + end[1]) / 2;
        if (null == result) {
            return new double[]{x, y};
        } else {
            result[0] = x;
            result[1] = y;
            return result;
        }
    }

    public double[] midPoint() {
        return midPoint(null);
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
        return new Node(midPoint());
    }

    @Override
    public String toString() {
        String endStr = (null == succ || null == getEnd()) ? "NULL" : getEnd().toString();
        String startStr = (null == start) ? "NULL" : start.toString();
        return String.format("Segment2D(%d)[h:(%s), r:(%s)]", id, startStr, endStr);
    }

    public double[] outNormal() {
        double[] result = Math2D.subs(getEnd().coord, start.coord, null);
        Math2D.normalize(result, result);
        return result;
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
