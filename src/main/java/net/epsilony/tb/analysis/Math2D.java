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

package net.epsilony.tb.analysis;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

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
            results = new double[] { v1[0] - v2[0], v1[1] - v2[1] };
        } else {
            results[0] = v1[0] - v2[0];
            results[1] = v1[1] - v2[1];
        }
        return results;
    }

    public static double[] adds(double[] v1, double[] v2, double[] results) {
        if (results == null) {
            results = new double[] { v1[0] + v2[0], v1[1] + v2[1] };
        } else {
            results[0] = v1[0] + v2[0];
            results[1] = v1[1] + v2[1];
        }
        return results;
    }

    public static double[] adds(double[] v1, double scale1, double[] v2, double scale2, double[] results) {
        if (results == null) {
            results = new double[] { v1[0] * scale1 + v2[0] * scale2, v1[1] * scale1 + v2[1] * scale2 };
        } else {
            results[0] = v1[0] * scale1 + v2[0] * scale2;
            results[1] = v1[1] * scale1 + v2[1] * scale2;
        }
        return results;
    }

    public static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * (x1 * y2 - y1 * x2 + x2 * y3 - y2 * x3 + x3 * y1 - y3 * x1);
    }

    public static double triangleArea(Triangle<?> tri) {
        double[] c1 = tri.getVertex(0).getCoord();
        double[] c2 = tri.getVertex(1).getCoord();
        double[] c3 = tri.getVertex(2).getCoord();
        return triangleArea(c1[0], c1[1], c2[0], c2[1], c3[0], c3[1]);
    }

    public static boolean isSegmentsIntersecting(double[] start1, double[] end1, double[] start2, double[] end2) {
        return isSegmentsIntersecting(start1[0], start1[1], end1[0], end1[1], start2[0], start2[1], end2[0], end2[1]);
    }

    public static boolean isSegmentsIntersecting(double h1x, double h1y, double r1x, double r1y, double h2x,
            double h2y, double r2x, double r2y) {
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

    public static double[] intersectionPoint(double[] startA, double[] endA, double[] startB, double[] endB,
            double[] result) {
        double deltaAx = endA[0] - startA[0];
        double deltaAy = endA[1] - startA[1];
        double deltaBx = endB[0] - startB[0];
        double deltaBy = endB[1] - startB[1];

        double crossDelta = cross(deltaAx, deltaAy, deltaBx, deltaBy);
        if (crossDelta == 0) {
            throw new IllegalArgumentException(
                    "the two segments are colinear or parrallel or one of them has zero length: " + "SegA :"
                            + Arrays.toString(startA) + "-" + Arrays.toString(endA) + " " + "SegB :"
                            + Arrays.toString(startB) + "-" + Arrays.toString(endB));
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

    public static <T> boolean isAnticlockwise(Iterable<T> iterable, Function<T, double[]> coordGetter) {
        return isAnticlockwise(iterable.iterator(), coordGetter);
    }

    public static boolean isAnticlockwise(Iterator<double[]> iter) {
        return isAnticlockwise(iter, Function.identity());
    }

    public static <T> boolean isAnticlockwise(Iterator<T> iter, Function<T, double[]> coordGetter) {
        double[] firstSegmentStart = coordGetter.apply(iter.next());
        double[] firstSegmentEnd = coordGetter.apply(iter.next());
        double[] p1;
        double[] p2 = firstSegmentStart;
        double[] p3 = firstSegmentEnd;
        double angle = 0;
        int tailRun = 2;
        while (true) {
            p1 = p2;
            p2 = p3;
            if (iter.hasNext()) {
                p3 = coordGetter.apply(iter.next());
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

    public static double projectionLength(double[] start, double[] end, double[] point) {
        double[] vec = subs(end, start, null);
        normalize(vec, vec);
        return dot(vec, subs(point, start, null));
    }

    public static double projectionParameter(double[] start, double[] end, double[] point) {
        if (Arrays.equals(start, point)) {
            return 0;
        }
        if (Arrays.equals(end, point)) {
            return 1;
        }
        return projectionLength(start, end, point) / distance(start, end);
    }

    public static double area(double[][] vertes) {
        double area = 0;
        double[] start;
        double[] end = vertes[vertes.length - 1];
        for (int i = 0; i < vertes.length; i++) {
            start = end;
            end = vertes[i];
            area += start[0] * end[1] - start[1] * end[0];
        }
        area /= 2;
        return area;
    }

    public static double area(Iterable<double[]> iter) {
        return area(iter.iterator(), Function.identity());
    }

    public static double area(Iterator<double[]> iter) {
        return area(iter, Function.identity());
    }

    public static <T> double area(Iterable<T> iter, Function<? super T, double[]> coordGetter) {
        return area(iter.iterator(), coordGetter);
    }

    public static <T> double area(Iterator<T> iter, Function<? super T, double[]> coordGetter) {
        double area = 0;
        double[] start;
        double[] end;
        double[] first;
        if (iter.hasNext()) {
            start = coordGetter.apply(iter.next());
            first = start;
        } else {
            throw new IllegalArgumentException();
        }
        if (iter.hasNext()) {
            end = coordGetter.apply(iter.next());
        } else {
            throw new IllegalArgumentException();
        }

        if (!iter.hasNext()) {
            throw new IllegalArgumentException();
        }
        do {
            area += start[0] * end[1] - start[1] * end[0];
            if (!iter.hasNext()) {
                break;
            }
            start = end;
            end = coordGetter.apply(iter.next());
        } while (true);
        area += end[0] * first[1] - end[1] * first[0];
        area /= 2;
        return area;
    }

    public static double[] centroid(double[][] vertes, double[] result) {

        double c_x = 0;
        double c_y = 0;
        double area = 0;
        for (int i = 0; i < vertes.length; i++) {
            int i_p = (i + 1) % vertes.length;
            double x_i = vertes[i][0];
            double y_i = vertes[i][1];
            double x_i_p = vertes[i_p][0];
            double y_i_p = vertes[i_p][1];
            double td = x_i * y_i_p - x_i_p * y_i;
            c_x += (x_i + x_i_p) * td;
            c_y += (y_i + y_i_p) * td;
            area += td;
        }
        area /= 2;
        c_x /= 6 * area;
        c_y /= 6 * area;
        if (result == null) {
            return new double[] { c_x, c_y };
        } else {
            result[0] = c_x;
            result[1] = c_y;
            return result;
        }
    }

    public static double[] vectorUnitOutNormal(double[] vector, double[] result) {
        if (null == result) {
            result = new double[2];
        }
        double dx = vector[0];
        double dy = vector[1];
        double length = measureLength(vector);
        result[0] = dy / length;
        result[1] = -dx / length;
        return result;
    }
}
