/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class GaussLegendre {

    public static final int MAXPOINTS = 5;
    public static final int MINPOINTS = 1;
    private static final double[][] points = new double[][]{
        {0},
        {-sqrt(3) / 3, sqrt(3) / 3},
        {-sqrt(15) / 5, 0, sqrt(15) / 5},
        {-sqrt(525 + 70 * sqrt(30)) / 35, -sqrt(525 - 70 * sqrt(30)) / 35,
            sqrt(525 - 70 * sqrt(30)) / 35, sqrt(525 + 70 * sqrt(30)) / 35},
        {-sqrt(245 + 14 * sqrt(70)) / 21, -sqrt(245 - 14 * sqrt(70)) / 21,
            0,
            sqrt(245 - 14 * sqrt(70)) / 21, sqrt(245 + 14 * sqrt(70)) / 21}};
    private static final double[][] weights = new double[][]{
        {2},
        {1, 1},
        {5 / 9d, 8 / 9d, 5 / 9d},
        {(18 - sqrt(30)) / 36, (18 + sqrt(30)) / 36,
            (18 + sqrt(30)) / 36, (18 - sqrt(30)) / 36},
        {(322 - 13 * sqrt(70)) / 900, (322 + 13 * sqrt(70)) / 900,
            128 / 225d,
            (322 + 13 * sqrt(70)) / 900, (322 - 13 * sqrt(70)) / 900}};

    public static boolean isPointsNumSupported(int num) {
        if (num < MINPOINTS || num > MAXPOINTS) {
            return false;
        }

        return true;
    }

    public static void checkPointsNum(int num) {
        if (isPointsNumSupported(num)) {
            return;
        }

        if (num < 1) {
            throw new IllegalArgumentException("points number must be >= 1, not " + num);
        }
        throw new UnsupportedOperationException(
                "The quadrature points number:" + num + " is not supported yet");
    }

    public static boolean isDegreeSupported(int degree) {
        int pointsNum = pointsNum(degree);
        return isPointsNumSupported(pointsNum);
    }

    public static void checkDegree(int degree) {
        if (isDegreeSupported(degree)) {
            return;
        }
        if (degree < 1) {
            throw new IllegalArgumentException("degree must be > 1, not " + degree);
        }

        throw new UnsupportedOperationException(
                "The quadrature degree:" + degree + " is not supported yet");
    }

    public static double[][] pointsWeightsByDegree(int degree) {
        int num = pointsNum(degree);
        return pointsWeightsByNum(num);
    }

    public static double[][] pointsWeightsByNum(int num) {
        isPointsNumSupported(num);
        return new double[][]{Arrays.copyOf(points[num - 1], num), Arrays.copyOf(weights[num - 1], num)};
    }

    public static int pointsNum(int degree) {
        int num = (int) Math.ceil((degree + 1) / 2.0);
        return num;
    }
}
