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

package net.epsilony.tb.quadrature;

import java.util.Arrays;
import java.util.Iterator;

import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.winged.Triangle;

/**
 * <p>
 * the data is copy from: </ br> Table 10.5 Dunavant quadrature for area
 * coordinate triangle , p275,Chapter 10, Finite Element Analysis with Error
 * Estimators, Ed Akin, Elsevere.
 * </p>
 * 
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public class SymmetricTriangleQuadratureUtils {

    public final static int MAX_ALGEBRAIC_ACCURACY = 8;
    public final static int MIN_ALGEBRAIC_ACCURACY = 1;
    private final static int[] numPts = new int[] { 1, 3, 4, 6, 7, 12, 13, 16 };
    private final static double[][] weights = new double[][] {
            { 1 },
            { 1 / 3d, 1 / 3d, 1 / 3d },
            { -27 / 48d, 25 / 48d, 25 / 48d, 25 / 48d },
            { 0.109951743655322, 0.109951743655322, 0.109951743655322, 0.223381589678011, 0.223381589678011,
                    0.223381589678011 },
            { 0.225000000000000, 0.125939180544827, 0.125939180544827, 0.125939180544827, 0.132394152788506,
                    0.132394152788506, 0.132394152788506 },
            { 0.050844906370207, 0.050844906370207, 0.050844906370207, 0.116786275726379, 0.116786275726379,
                    0.116786275726379, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374,
                    0.082851075618374, 0.082851075618374 },
            { -0.149570044467682, 0.175615257433208, 0.175615257433208, 0.175615257433208, 0.053347235608838,
                    0.053347235608838, 0.053347235608838, 0.077113760890257, 0.077113760890257, 0.077113760890257,
                    0.077113760890257, 0.077113760890257, 0.077113760890257 },
            { 0.144315607677787, 0.095091634267285, 0.095091634267285, 0.095091634267285, 0.103217370534718,
                    0.103217370534718, 0.103217370534718, 0.032458497623198, 0.032458497623198, 0.032458497623198,
                    0.027230314174435, 0.027230314174435, 0.027230314174435, 0.027230314174435, 0.027230314174435,
                    0.027230314174435 }, };
    private final static double[][] barycentricCoordinates = new double[][] {
            { 1 / 3d, 1 / 3d, 1 / 3d },
            { 2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d },
            { 1 / 3d, 1 / 3d, 1 / 3d, 0.6, 0.2, 0.2, 0.2, 0.6, 0.2, 0.2, 0.2, 0.6 },
            { 0.816847572980459, 0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459,
                    0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459, 0.108103018168070,
                    0.445948490915965, 0.445948490915965, 0.445948490915965, 0.108103018168070, 0.445948490915965,
                    0.445948490915965, 0.445948490915965, 0.108103018168070 },
            { 1 / 3d, 1 / 3d, 1 / 3d, 0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456,
                    0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456, 0.797426985353087,
                    0.059715871789770, 0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770,
                    0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770 },
            { 0.873821971016996, 0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996,
                    0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996, 0.501426509658179,
                    0.249286745170910, 0.249286745170910, 0.249286745170910, 0.501426509658179, 0.249286745170910,
                    0.249286745170910, 0.249286745170910, 0.501426509658179, 0.636502499121399, 0.310352451033784,
                    0.053145049844817, 0.636502499121399, 0.053145049844817, 0.310352451033784, 0.310352451033784,
                    0.636502499121399, 0.053145049844817, 0.310352451033784, 0.053145049844817, 0.636502499121399,
                    0.053145049844817, 0.636502499121399, 0.310352451033784, 0.053145049844817, 0.310352451033784,
                    0.636502499121399 },
            { 1 / 3d, 1 / 3d, 1 / 3d, 0.479308067841920, 0.260345966079040, 0.260345966079040, 0.260345966079040,
                    0.479308067841920, 0.260345966079040, 0.260345966079040, 0.260345966079040, 0.479308067841920,
                    0.869739794195568, 0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568,
                    0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568, 0.638444188569810,
                    0.312865496004874, 0.048690315425316, 0.638444188569810, 0.048690315425316, 0.312865496004874,
                    0.312865496004874, 0.638444188569810, 0.048690315425316, 0.312865496004874, 0.048690315425316,
                    0.638444188569810, 0.048690315425316, 0.638444188569810, 0.312865496004874, 0.048690315425316,
                    0.312865496004874, 0.638444188569810 },
            { 1 / 3d, 1 / 3d, 1 / 3d, 0.081414823414554, 0.459292588292723, 0.459292588292723, 0.459292588292723,
                    0.081414823414554, 0.459292588292723, 0.459292588292723, 0.459292588292723, 0.081414823414554,
                    0.658861384496480, 0.170569307751760, 0.170569307751760, 0.170569307751760, 0.658861384496480,
                    0.170569307751760, 0.170569307751760, 0.170569307751760, 0.658861384496480, 0.898905543365938,
                    0.050547228317031, 0.050547228317031, 0.050547228317031, 0.898905543365938, 0.050547228317031,
                    0.050547228317031, 0.050547228317031, 0.898905543365938, 0.008394777409958, 0.263112829634638,
                    0.728492392955404, 0.008394777409958, 0.728492392955404, 0.263112829634638, 0.263112829634638,
                    0.008394777409958, 0.728492392955404, 0.263112829634638, 0.728492392955404, 0.008394777409958,
                    0.728492392955404, 0.008394777409958, 0.263112829634638, 0.728492392955404, 0.263112829634638,
                    0.008394777409958 } };

    /**
     * 获取degree阶代数精度所对应的三角形对称积分点数
     * 
     * @param degree
     *            积分的代数精度 应属于闭区间[{@link #MIN_ALGEBRAIC_ACCURACY},
     *            {@link #MAX_ALGEGRAIC_ACCURACY}]
     * @return
     */
    public static int numPointsByAlgebraicAccuracy(int degree) {
        return numPts[degree - 1];
    }

    public static double[] barycentricCoordinates(int degree) {
        double[] results = Arrays.copyOf(barycentricCoordinates[degree - 1], barycentricCoordinates[degree - 1].length);
        return results;
    }

    public static double[] cartesianCoordinate(double x1, double y1, double x2, double y2, double x3, double y3,
            int degree, int index, double[] results) {
        double[] coords = barycentricCoordinates[degree - 1];
        if (null == results) {
            results = new double[2];
        }
        results[0] = coords[index * 3] * x1 + coords[index * 3 + 1] * x2 + coords[index * 3 + 2] * x3;
        results[1] = coords[index * 3] * y1 + coords[index * 3 + 1] * y2 + coords[index * 3 + 2] * y3;
        return results;
    }

    public static double[] cartesianCoordinate(Triangle tri, int degree, int index, double[] result) {
        double[] coord1 = tri.getVertex(0).getCoord();
        double[] coord2 = tri.getVertex(1).getCoord();
        double[] coord3 = tri.getVertex(2).getCoord();
        return cartesianCoordinate(coord1[0], coord1[1], coord2[0], coord2[1], coord3[0], coord3[1], degree, index,
                result);
    }

    public static double getWeight(int degree, int index) {
        return weights[degree - 1][index];
    }

    public static class QuadIterator implements Iterator<QuadraturePoint> {

        double x1, y1, x2, y2, x3, y3;
        int size, nextIndex, degree;
        double area;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public QuadraturePoint next() {
            QuadraturePoint result = new QuadraturePoint();
            result.weight = area * getWeight(degree, nextIndex);
            result.coord = cartesianCoordinate(x1, y1, x2, y2, x3, y3, degree, nextIndex, null);
            nextIndex++;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public QuadIterator(double x1, double y1, double x2, double y2, double x3, double y3, int degree) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
            this.degree = degree;
            area = Math2D.triangleArea(x1, y1, x2, y2, x3, y3);
            size = numPointsByAlgebraicAccuracy(degree);
            nextIndex = 0;
        }
    }
}
