/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.epsilony.tb.solid;

import net.epsilony.tb.analysis.Math2D;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ArcSegment2D extends RawSegment {

    double radius;
    boolean centerOnChordLeft = true;// chord is a linear segment start from
                                     // start node and end at end node

    public double[] calcCenter(double[] result) {
        if (null == result) {
            result = new double[2];
        }
        double[] startCoord = getStart().getCoord();
        double[] endCoord = getEnd().getCoord();
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

    public double distanceTo(double x, double y) {
        double[] center = calcCenter(null);
        double[] startCoord = getStart().getCoord();
        double[] endCoord = getEnd().getCoord();
        double vecX = x - center[0];
        double vecY = y - center[1];
        double crossToStart = Math2D.cross(vecX, vecY, startCoord[0] - center[0], startCoord[1] - center[1]);
        double crossToEnd = Math2D.cross(vecX, vecY, endCoord[0] - center[0], endCoord[1] - center[1]);
        boolean betwean = crossToStart * crossToEnd < 0;
        if (betwean) {
            return radius - Math.sqrt(vecX * vecX + vecY * vecY);
        } else {
            return Math.min(Math2D.distance(x, y, startCoord[0], startCoord[1]),
                    Math2D.distance(x, y, endCoord[0], endCoord[1]));
        }
    }

    public void bisect() {
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
        double[] startCoord = getStart().getCoord();
        double[] endCoord = getEnd().getCoord();
        double chordLengthSquare = Math2D.distanceSquare(startCoord, endCoord);
        double centerAngleCosine = (2 * radius * radius - chordLengthSquare) / 2 / (radius * radius);
        if (Math.abs(centerAngleCosine) > 1) {
            throw new IllegalStateException("Radius too small, chord length: " + Math.sqrt(chordLengthSquare)
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
        double[] startCoord = getStart().getCoord();
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
