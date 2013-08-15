/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import java.util.Arrays;
import net.epsilony.tb.MiscellaneousUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TripleSpline implements RadialFunctionCore {

    int diffOrder;
    private static double[][] coefs = new double[][]{
        {2 / 3.0, 0, -4, 4},
        {4 / 3.0, -4, 4, -4 / 3.0}};
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
            results[0] = func1.value(x);
            if (diffOrder > 0) {
                results[1] = func1Diff.value(x);
            }
        } else {
            results[0] = func2.value(x);
            if (diffOrder > 0) {
                results[1] = func2Diff.value(x);
            }
        }
        return results;
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
    public RadialFunctionCore synchronizeClone() {
        TripleSpline result = new TripleSpline();
        result.setDiffOrder(getDiffOrder());
        return result;
    }

    @Override
    public String toString() {
        return MiscellaneousUtils.simpleToString(this);
    }
}
