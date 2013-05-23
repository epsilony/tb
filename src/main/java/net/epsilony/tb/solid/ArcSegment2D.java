/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ArcSegment2D extends AbstractSegment2D {

    double radius;
    boolean centerOnChordLeft = true;//chord is a linear segment start from start node and end at end node

    public double[] calcCenter(double[] result) {
        if (null == result) {
            result = new double[2];
        }
        double[] startCoord = getStartCoord();
        double[] endCoord = getEndCoord();
        double dx = endCoord[0] - startCoord[0];
        double dy = endCoord[1] - startCoord[1];
        double midToCenterX, midToCenterY;
        if (centerOnChordLeft) {
            midToCenterX = -dy;
            midToCenterY = dx;
        } else {
            midToCenterX = dy;
            midToCenterY = -dx;
        }
        double centerToMidDistance = Math.sqrt(radius * radius - dx * dx / 4 - dy * dy / 4);
        double tLength = Math.sqrt(midToCenterX * midToCenterX + midToCenterY * midToCenterY);
        double tScale = centerToMidDistance / tLength;
        midToCenterX *= tScale;
        midToCenterY *= tScale;
        double midX = (endCoord[0] + startCoord[0]) / 2;
        double midY = (endCoord[1] + startCoord[1]) / 2;
        result[0] = midX + midToCenterX;
        result[1] = midY + midToCenterY;
        return result;
    }

    @Override
    public double distanceTo(double x, double y) {
        double[] center = calcCenter(null);
        double[] startCoord = getStartCoord();
        double[] endCoord = getEndCoord();
        double vecX = x - center[0];
        double vecY = y - center[1];
        double crossToStart = Math2D.cross(vecX, vecY, startCoord[0] - center[0], startCoord[1] - center[1]);
        double crossToEnd = Math2D.cross(vecX, vecY, endCoord[0] - center[0], endCoord[1] - center[1]);
        boolean betwean = crossToStart * crossToEnd < 0;
        if (betwean) {
            return radius - Math.sqrt(vecX * vecX + vecY * vecY);
        } else {
            return Math.min(
                    Math2D.distance(x, y, startCoord[0], startCoord[1]),
                    Math2D.distance(x, y, endCoord[0], endCoord[1]));
        }
    }

    @Override
    public ArcSegment2D bisectionAndReturnNewSuccessor() {
        int diffOrderBack = getDiffOrder();
        setDiffOrder(0);
        double[] midPoint = values(0.5, null);
        setDiffOrder(diffOrderBack);
        ArcSegment2D newSucc = new ArcSegment2D();
        newSucc.setStart(new Node(midPoint));
        newSucc.setSucc(succ);
        succ.setPred(newSucc);
        succ = newSucc;
        newSucc.setPred(this);
        newSucc.setDiffOrder(diffOrder);
        newSucc.setRadius(radius);
        newSucc.setCenterOnChordLeft(centerOnChordLeft);
        return newSucc;
    }

    @Override
    public double[] values(double t, double[] results) {
        if (null == results) {
            results = new double[2 * (diffOrder + 1)];
        }
        calcCenter(results);
        double centerX = results[0];
        double centerY = results[1];
        double centerAngle = calcCenterAngle(centerX, centerY);
        double startAmplitudeAngle = calcStartAmplitudeAngle(centerX, centerY);
        double resultAmplitudeAngle = startAmplitudeAngle + centerAngle * t;
        double vX = radius * Math.cos(resultAmplitudeAngle);
        double vY = radius * Math.sin(resultAmplitudeAngle);
        double resultX = vX + centerX;
        double resultY = vY + centerY;
        results[0] = resultX;
        results[1] = resultY;
        if (diffOrder >= 1) {
            results[2] = -vY * centerAngle;
            results[3] = vX * centerAngle;
        }
        return results;
    }

    public double calcCenterAngle() {
        double[] center = calcCenter(null);
        return calcCenterAngle(center[0], center[1]);
    }

    public double calcCenterAngle(double centerX, double centerY) {
        double[] startCoord = getStartCoord();
        double[] endCoord = getEndCoord();
        double chordLengthSquare = Math2D.distanceSquare(startCoord, endCoord);
        double centerAngleCosine = (2 * radius * radius - chordLengthSquare) / 2 / (radius * radius);
        if (Math.abs(centerAngleCosine) > 1) {
            throw new IllegalStateException(
                    "Radius too small, chord length: "
                    + Math.sqrt(chordLengthSquare)
                    + "radius:" + radius);
        }
        double centerAngle = Math.acos(centerAngleCosine);
        if (!centerOnChordLeft) {
            centerAngle = -centerAngle;
        }
        return centerAngle;
    }

    public double calcStartAmplitudeAngle() {
        double[] center = calcCenter(null);
        return calcStartAmplitudeAngle(center[0], center[1]);
    }

    public double calcStartAmplitudeAngle(double centerX, double centerY) {
        double[] startCoord = getStartCoord();
        return Math.atan2(startCoord[1] - centerY, startCoord[0] - centerX);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * chord is a linear segment start from start node and end at end node
     *
     * @return
     */
    public boolean isCenterOnChordLeft() {
        return centerOnChordLeft;
    }

    /**
     * chord is a linear segment start from start node and end at end node
     *
     * @return
     */
    public void setCenterOnChordLeft(boolean centerOnTheLeft) {
        this.centerOnChordLeft = centerOnTheLeft;
    }
}
