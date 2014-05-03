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

package net.epsilony.tb.common_func;

import java.util.Arrays;

import net.epsilony.tb.MiscellaneousUtils;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TripleSpline implements RadialBasisCore {

    int diffOrder;
    private static double[][] coefs = new double[][] { { 2 / 3.0, 0, -4, 4 }, { 4 / 3.0, -4, 4, -4 / 3.0 } };
    PolynomialFunction func1 = new PolynomialFunction(coefs[0]);
    PolynomialFunction func2 = new PolynomialFunction(coefs[1]);
    PolynomialFunction func1Diff = func1.polynomialDerivative();
    PolynomialFunction func2Diff = func2.polynomialDerivative();

    @Override
    public double[] valuesByDistance(double distance, double[] results) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance should be >= 0, not" + distance);
        }

        if (null == results) {
            results = new double[diffOrder + 1];
        }

        if (distance >= 1) {
            Arrays.fill(results, 0);
            return results;
        }

        double x = distance;
        if (x <= 0.5) {
            results[0] = func1(x);
            if (diffOrder > 0) {
                results[1] = func1Diff(x);
            }
        } else {
            results[0] = func2(x);
            if (diffOrder > 0) {
                results[1] = func2Diff(x);
            }
        }
        return results;
    }

    public static double func1(double x) {
        // coefs:
        // 2 / 3.0, 0, -4, 4
        return 2 / 3.0 + (-4 + 4 * x) * x * x;
    }

    public static double func1Diff(double x) {
        return x * (-8 + 12 * x);
    }

    public static double func2(double x) {
        // coefs:
        // 4 / 3.0, -4, 4, -4 / 3.0
        return 4 / 3.0 + x * (-4 + x * (4 - 4 / 3.0 * x));
    }

    public static double func2Diff(double x) {
        return -4 + x * (8 - 4 * x);
    }

    @Override
    public double[] valuesByDistanceSquare(double distanceSquare, double[] results) {
        if (distanceSquare < 0) {
            throw new IllegalArgumentException("distanceSquare should be >= 0, not" + distanceSquare);
        }

        if (null == results) {
            results = new double[diffOrder + 1];
        }

        if (distanceSquare >= 1) {
            Arrays.fill(results, 0);
            return results;
        }

        double u = distanceSquare;
        double x = Math.sqrt(u);
        if (u <= 0.25) {
            results[0] = 2 / 3.0 - 4 * u + 4 * u * x;
            if (diffOrder > 0) {
                results[1] = -4 + 6 * x;
            }
        } else {
            results[0] = 4 / 3.0 - 4 * x + 4 * u - 4 / 3.0 * u * x;
            if (diffOrder > 0) {
                results[1] = -2 / x + 4 - 2 * x;
            }
        }
        return results;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("only support diffOrder that is 0 or 1, not " + diffOrder);
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public String toString() {
        return MiscellaneousUtils.simpleToString(this);
    }
}
