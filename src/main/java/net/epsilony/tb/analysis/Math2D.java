/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

import java.util.Arrays;
import java.util.Iterator;
import net.epsilony.tb.solid.winged.Triangle;

/**
 *
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public class Math2D {

    public static double dot(double[] v1, double[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1];
    }

    public static double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public static double measureLength(double[] vec) {
        return Math.sqrt(dot(vec, vec));
    }

    public static double[] normalize(double[] vec, double[] result) {
        double length = measureLength(vec);
        result = scale(vec, 1 / length, result);
        return result;
    }

    public static double cross(double[] v1, double[] v2) {
        return v1[0] * v2[1] - v1[1] * v2[0];
    }

    public static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSquare(x1, y1, x2, y2));
    }

    public static double distanceSquare(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public static double distanceSquare(double[] v1, double[] v2) {
        return distanceSquare(v1[0], v1[1], v2[0], v2[1]);
    }

    public static double distance(double[] xy1, double[] xy2) {
        return distance(xy1[0], xy1[1], xy2[0], xy2[1]);
    }

    public static double[] distanceAndPartDiffs(double x, double y, double x0, double y0, double[] results) {
        if (null == results) {
            results = new double[3];
        }
        double dst = distance(x, y, x0, y0);
        results[0] = dst;
        if (dst != 0) {
            results[1] = (x - x0) / dst;
            results[2] = (y - y0) / dst;
        } else {
            results[1] = 0;
            results[2] = 0;
        }
        return results;
    }

    public static double[] distanceAndPartDiffs(double[] xy, double[] to, double[] results) {
        return distanceAndPartDiffs(xy[0], xy[1], to[0], to[1], results);
    }

    public static double[] scale(double[] vec, double scale, double[] result) {
        if (null == result) {
            result = new double[2];
        }
        result[0] = vec[0] * scale;
        result[1] = vec[1] * scale;
        return result;
    }

    public static double[] subs(double[] v1, double[] v2, double[] results) {
        if (results == null) {
            results = new double[]{v1[0] - v2[0], v1[1] - v2[1]};
        } else {
            results[0] = v1[0] - v2[0];
            results[1] = v1[1] - v2[1];
        }
        return results;
    }

    public static double[] adds(double[] v1, double[] v2, double[] results) {
        if (results == null) {
            results = new double[]{v1[0] + v2[0], v1[1] + v2[1]};
        } else {
            results[0] = v1[0] + v2[0];
            results[1] = v1[1] + v2[1];
        }
        return results;
    }

    public static double[] adds(double[] v1, double scale1, double[] v2, double scale2, double[] results) {
        if (results == null) {
            results = new double[]{v1[0] * scale1 + v2[0] * scale2, v1[1] * scale1 + v2[1] * scale2};
        } else {
            results[0] = v1[0] * scale1 + v2[0] * scale2;
            results[1] = v1[1] * scale1 + v2[1] * scale2;
        }
        return results;
    }

    public static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * cross(x2 - x1, y2 - y1, x3 - x1, y3 - y1);
    }

    public static double triangleArea(Triangle tri) {
        double[] c1 = tri.getVertex(0).getCoord();
        double[] c2 = tri.getVertex(1).getCoord();
        double[] c3 = tri.getVertex(2).getCoord();
        return triangleArea(c1[0], c1[1], c2[0], c2[1], c3[0], c3[1]);
    }

    public static boolean isSegmentsIntersecting(double[] start1, double[] end1, double[] start2, double[] end2) {
        return isSegmentsIntersecting(start1[0], start1[1], end1[0], end1[1], start2[0], start2[1], end2[0], end2[1]);
    }

    public static boolean isSegmentsIntersecting(
            double h1x, double h1y,
            double r1x, double r1y,
            double h2x, double h2y,
            double r2x, double r2y) {
        double u1 = r1x - h1x;
        double u2 = r1y - h1y;
        double v1 = r2x - h2x;
        double v2 = r2y - h2y;
        double w1 = h2x - h1x;
        double w2 = h2y - h1y;
        double denorm = v1 * u2 - v2 * u1;
        if (0 == denorm) {// coincident or just parrel  
            if (w1 * u2 - w2 * u1 != 0) {
                return false;
            }
            double d1 = u1;
            double d2 = w1;
            double d3 = r2x - h1x;
            if (d1 == 0) {
                d1 = u2;
                d2 = w2;
                d3 = r2y - h1y;
            }
            double t = d2 / d1;
            if (t <= 1 && t >= 0) {
                return true;
            }
            t = d3 / d1;
            if (t <= 1 && t >= 0) {
                return true;
            }
            if (d2 * d3 < 0) {
                return true;
            }
            return false;

        }
        double t1 = -(v2 * w1 - v1 * w2) / denorm;
        double t2 = (u1 * w2 - u2 * w1) / denorm;
        if (t1 < 0 || t1 > 1 || t2 < 0 || t2 > 1) {
            return false;
        }
        return true;
    }

    public static double[] pointOnSegment(double[] start, double[] end, double t, double[] result) {
        if (null == result) {
            result = new double[2];
        }
        result[0] = start[0] * (1 - t) + end[0] * t;
        result[1] = start[1] * (1 - t) + end[1] * t;
        return result;
    }

    public static double[] intersectionPoint(
            double[] startA, double[] endA,
            double[] startB, double[] endB,
            double[] result) {
        double deltaAx = endA[0] - startA[0];
        double deltaAy = endA[1] - startA[1];
        double deltaBx = endB[0] - startB[0];
        double deltaBy = endB[1] - startB[1];

        double crossDelta = cross(deltaAx, deltaAy, deltaBx, deltaBy);
        if (crossDelta == 0) {
            throw new IllegalArgumentException(
                    "the two segments are colinear or parrallel or one of them has zero length: "
                    + "SegA :" + Arrays.toString(startA) + "-" + Arrays.toString(endA) + " "
                    + "SegB :" + Arrays.toString(startB) + "-" + Arrays.toString(endB));
        }

        double uA = cross(startB[0] - startA[0], startB[1] - startA[1], deltaBx, deltaBy) / crossDelta;

        return pointOnSegment(startA, endA, uA, result);
    }

    public static double cos(double x1, double y1, double x2, double y2) {
        return (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
    }

    public static boolean isAnticlockwise(Iterable<double[]> simplePolygonVertes) {
        Iterator<double[]> iter = simplePolygonVertes.iterator();
        return isAnticlockwise(iter);
    }

    public static boolean isAnticlockwise(Iterator<double[]> iter) {
        double[] firstSegmentStart = iter.next();
        double[] firstSegmentEnd = iter.next();
        double[] p1;
        double[] p2 = firstSegmentStart;
        double[] p3 = firstSegmentEnd;
        double angle = 0;
        int tailRun = 2;
        while (true) {
            p1 = p2;
            p2 = p3;
            if (iter.hasNext()) {
                p3 = iter.next();
            } else {
                if (tailRun <= 0) {
                    break;
                }
                if (tailRun == 2) {
                    p3 = firstSegmentStart;
                } else {
                    p3 = firstSegmentEnd;
                }
                tailRun--;
            }
            double v1x = p2[0] - p1[0];
            double v1y = p2[1] - p1[1];
            double v2x = p3[0] - p2[0];
            double v2y = p3[1] - p2[1];
            double deltaAngle = deltaAngle(v1x, v1y, v2x, v2y);
            angle += deltaAngle;
        }
        return angle > 0;
    }

    public static double deltaAngle(double v1x, double v1y, double v2x, double v2y) {
        double crossValue = cross(v1x, v1y, v2x, v2y);
        double cosineValue = cos(v1x, v1y, v2x, v2y);
        double deltaAngle = Math.acos(cosineValue) * Math.signum(crossValue);
        return deltaAngle;
    }
}
