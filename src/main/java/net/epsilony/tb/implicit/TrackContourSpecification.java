/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import net.epsilony.tb.analysis.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TrackContourSpecification {

    public static final double DEFAULT_HEAD_PERPENDICULAR_TOLERENCE = Math.PI / 6;
    public static final double MIN_MAX_SEGMENT_LENGTH_RATIO = 0.01;
    public static double defaultMaxSegmentLength = 1.5;
    public static final double DEFAULT_MAX_SEGMENT_CURVE = Math.PI / 6;
    public static final double DEFAULT_PERPENDICULAR_TOLERENCE = Math.PI / 3;
    public static final double DEFAULT_EXPECT_CURVE = Math.PI / 12;
    public static final double DEFAULT_CLOSE_HEAD_SEARCH_ANGLE = Math.PI / 2;
    //
    private double headPerpendicularTolerence = DEFAULT_HEAD_PERPENDICULAR_TOLERENCE;
    private double headPerpendicularLowerLimit = Math.cos(headPerpendicularTolerence);
    private double maxSegmentLength = defaultMaxSegmentLength;
    private double minSegmentLength = maxSegmentLength * MIN_MAX_SEGMENT_LENGTH_RATIO;
    private double maxSegmentCurve = DEFAULT_MAX_SEGMENT_CURVE;
    private double segmentCurveLowerBound = Math.cos(maxSegmentCurve);
    private double perpendicularTolerence = DEFAULT_PERPENDICULAR_TOLERENCE;
    private double perpendicularLowerLimit = Math.cos(perpendicularTolerence);
    private double expectSegmentCurve = DEFAULT_EXPECT_CURVE;
    private double expectCurveParameter = Math.sqrt(1 - Math.cos(expectSegmentCurve));
    private double closeHeadSearchAngle = DEFAULT_CLOSE_HEAD_SEARCH_ANGLE;
    private double closeHeadSearchLowerLimit = Math.cos(closeHeadSearchAngle);
    //
    private static final double ROUGH_POINT_DISTANCE_SHRINK = 0.8;

    public double genNextRoughPointDistance(ContourNode ndA, ContourNode ndB) {
        double d = Math2D.distance(ndA.getCoord(), ndB.getCoord());
        double[] funcValueA = ndA.getFunctionValue();
        double[] funcValueB = ndB.getFunctionValue();
        double gax = funcValueA[1];
        double gay = funcValueA[2];
        double gbx = funcValueB[1];
        double gby = funcValueB[2];
        double gradCos = Math2D.cos(gax, gay, gbx, gby);

        double result = ROUGH_POINT_DISTANCE_SHRINK * d * expectCurveParameter / Math.sqrt(1 - gradCos);

        if (result > maxSegmentLength) {
            result = maxSegmentLength;
        }

        if (result < minSegmentLength) {
            result = minSegmentLength;
        }
        return result;
    }

    public boolean isOtherHeadInCandidateDirection(
            ContourNode otherHead,
            double[] nodeCoord,
            double[] nodeUnitNormal) {

        double[] headCoord = otherHead.getCoord();

        double[] vec = Math2D.subs(headCoord, nodeCoord, null);
        Math2D.normalize(vec, vec);
        double outer = Math2D.cross(nodeUnitNormal, vec);
        if (outer < closeHeadSearchLowerLimit) {
            return false;
        }

        double[] headNormal = Arrays.copyOfRange(otherHead.getFunctionValue(), 1, 3);
        Math2D.normalize(headNormal, headNormal);
        double inner = Math2D.dot(headNormal, nodeUnitNormal);
        if (inner <= 0) {
            return false;
        }

        return true;
    }

    public boolean isHeadPointNormalEligible(ContourNode head, double[] unitRoughNextDirection) {
        double[] funcV = head.getFunctionValue();
        double nx = funcV[1];
        double ny = funcV[2];
        double nNorm = Math.sqrt(nx * nx + ny * ny);
        nx /= nNorm;
        ny /= nNorm;
        double mx = unitRoughNextDirection[0];
        double my = unitRoughNextDirection[1];
        double outer = Math2D.cross(nx, ny, mx, my);
        if (outer < headPerpendicularLowerLimit) {
            return false;
        }
        return true;
    }

    public boolean isSegmentEligible(ContourNode start, ContourNode end) {

        double[] startFuncV = start.getFunctionValue();
        double[] endFuncV = end.getFunctionValue();
        double[] startNormal = Arrays.copyOfRange(startFuncV, 1, startFuncV.length);
        double[] endNormal = Arrays.copyOfRange(endFuncV, 1, endFuncV.length);
        Math2D.normalize(startNormal, startNormal);
        Math2D.normalize(endNormal, endNormal);

        double curveCos = Math2D.dot(startNormal, endNormal);
        if (curveCos < segmentCurveLowerBound) {
            return false;
        }

        double[] startCoord = start.getCoord();
        double[] endCoord = end.getCoord();
        double[] vec = Math2D.subs(endCoord, startCoord, null);
        Math2D.normalize(vec, vec);

        double outer = Math2D.cross(startNormal, vec);
        if (outer < perpendicularLowerLimit) {
            return false;
        }
        outer = Math2D.cross(endNormal, vec);
        if (outer < perpendicularLowerLimit) {
            return false;
        }

        return true;
    }

    public double getCloseHeadSearchAngle() {
        return closeHeadSearchAngle;
    }

    public void setCloseHeadSearchAngle(double closeHeadSearchAngle) {
        if (closeHeadSearchAngle <= 0 || closeHeadSearchAngle >= Math.PI / 2) {
            throw new IllegalArgumentException("should be in (0,pi/2), not " + closeHeadSearchAngle);
        }
        closeHeadSearchLowerLimit = Math.cos(closeHeadSearchAngle);
        this.closeHeadSearchAngle = closeHeadSearchAngle;
    }

    public double getExpectSegmentCurve() {
        return expectSegmentCurve;
    }

    public void setExpectSegmentCurve(double expectSegmentCurve) {
        if (expectSegmentCurve <= 0 || expectSegmentCurve >= Math.PI / 2) {
            throw new IllegalArgumentException("expect curver should be in (0, pi/2), not " + expectSegmentCurve);
        }
        expectCurveParameter = Math.sqrt(1 - Math.cos(expectSegmentCurve));
        this.expectSegmentCurve = expectSegmentCurve;
    }

    public double getHeadPerpendicularTolerence() {
        return headPerpendicularTolerence;
    }

    public void setHeadPerpendicularTolerence(double headPerpendicularTolerence) {
        if (headPerpendicularTolerence < 0 || headPerpendicularTolerence > Math.PI / 2) {
            throw new IllegalArgumentException("angle should be in (0,pi/2), not " + headPerpendicularTolerence);
        }
        this.headPerpendicularTolerence = headPerpendicularTolerence;
        this.headPerpendicularLowerLimit = Math.cos(headPerpendicularTolerence);
    }

    public void setMaxSegmentLength(double maxSegmentLength) {
        if (maxSegmentLength <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxSegmentLength = maxSegmentLength;
        if (minSegmentLength > maxSegmentLength) {
            minSegmentLength = maxSegmentLength;
        }
    }

    public void setMinSegmentLength(double minSegmentLength) {
        if (minSegmentLength < 0) {
            throw new IllegalArgumentException();
        }
        this.minSegmentLength = minSegmentLength;
        if (maxSegmentLength < minSegmentLength) {
            maxSegmentLength = minSegmentLength;
        }
    }

    public double getMaxSegmentLength() {
        return maxSegmentLength;
    }

    public double getMaxSegmentCurve() {
        return maxSegmentCurve;
    }

    public void setMaxSegmentCurve(double angle) {
        if (angle > Math.PI / 2 || angle < 0) {
            throw new IllegalArgumentException("should be in [0,pi/2], not" + angle);
        }
        segmentCurveLowerBound = Math.cos(maxSegmentCurve);
        this.maxSegmentCurve = angle;
    }

    public double getPerpendicularTolerence() {
        return perpendicularTolerence;
    }

    public void setPerpendicularTolerence(double perpendicularTolerence) {
        if (perpendicularTolerence > Math.PI / 2 || perpendicularTolerence < 0) {
            throw new IllegalArgumentException("should be in [0,pi/2], not" + perpendicularTolerence);
        }
        perpendicularLowerLimit = Math.cos(perpendicularTolerence);
        this.perpendicularTolerence = perpendicularTolerence;
    }

    public double getMinSegmentLength() {
        return minSegmentLength;
    }
}
