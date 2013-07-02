/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.analysis.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class GeneralLine2D<PS extends Segment<PS, ND>, ND extends Node> extends AbstractSegment<PS, ND> {

    public GeneralLine2D() {
    }

    public GeneralLine2D(ND node) {
        start = node;
    }

    public double length() {
        return Math2D.distance(getStart().getCoord(), getEnd().getCoord());
    }

    @Override
    public double[] values(double t, double[] results) {
        if (null == results) {
            results = new double[diffOrder * 2];
        }
        double[] startCoord = getStart().getCoord();
        double[] endCoord = getEnd().getCoord();
        Math2D.pointOnSegment(startCoord, endCoord, t, results);
        if (diffOrder >= 1) {
            results[2] = endCoord[0] - startCoord[0];
            results[3] = endCoord[1] - startCoord[1];
        }
        return results;
    }
}
