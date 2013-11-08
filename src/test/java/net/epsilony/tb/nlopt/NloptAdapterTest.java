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

package net.epsilony.tb.nlopt;

import static java.lang.Math.sqrt;
import java.util.Arrays;
//import java.util.Arrays;
import net.epsilony.tb.analysis.AbstractDifferentiableFunction;
import net.epsilony.tb.analysis.DifferentiableFunction;
import org.bridj.IntValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NloptAdapterTest {

    public NloptAdapterTest() {
    }

    @Test
    public void testStandardCase() {
        DifferentiableFunction objFunc = new AbstractDifferentiableFunction() {
            // int count = 0;
            @Override
            public int getInputDimension() {
                return 2;
            }

            @Override
            public int getOutputDimension() {
                return 1;
            }

            @Override
            public double[] value(double[] input, double[] output) {

                if (null == output) {
                    output = new double[getDiffOrder() * 2 + 1];
                }
                if (null == output) {
                    output = new double[getDiffOrder() * 2 + 1];
                }
                output[0] = sqrt(input[1]);
                if (diffOrder >= 1) {
                    output[1] = 0;
                    output[2] = 0.5 / sqrt(input[1]);
                }
                // System.out.println(count++ + ": " + Arrays.toString(input) +
                // ", " + Arrays.toString(output));
                return output;
            }
        };
        DifferentiableFunction constraints = new AbstractDifferentiableFunction() {
            double[][] abs = new double[][] { { 2, 0 }, { -1, 1 } };

            // int count;

            @Override
            public int getInputDimension() {
                return 2;
            }

            @Override
            public int getOutputDimension() {
                return 2;
            }

            @Override
            public double[] value(double[] input, double[] output) {
                if (output == null) {
                    output = new double[getDiffOrder() * getOutputDimension() * getInputDimension()
                            + getOutputDimension()];
                }
                double x = input[0];
                double y = input[1];
                for (int i = 0; i < getOutputDimension(); i++) {
                    double a = abs[i][0];
                    double b = abs[i][1];
                    double t = a * x + b;
                    output[i] = t * t * t - y;
                    output[getOutputDimension() * (i + 1)] = 3 * a * t * t;
                    output[getOutputDimension() * (i + 1) + 1] = -1;
                }
                // System.out.println("M" + count++ + ": " +
                // Arrays.toString(input) + ", " + Arrays.toString(output));
                return output;
            }
        };
        double[] constraintsTols = new double[] { 1e-8, 1e-8 };
        double[] lowerBounds = new double[] { Double.NEGATIVE_INFINITY, 0 };
        double RelativeXTolerence = 1e-4;
        double[] startPoint = new double[] { 1.234, 5.768 };

        NloptAdapter nloptAdapter = new NloptAdapter(NloptLibrary.NloptAlgorithm.NLOPT_LD_MMA, 2);
        nloptAdapter.setMinObjective(objFunc);
        nloptAdapter.setLowerBounds(lowerBounds);
        nloptAdapter.addInequalityVectorConstraint(constraints, constraintsTols);
        nloptAdapter.setRelativeXTolerence(RelativeXTolerence);
        double[] objValue = new double[1];
        IntValuedEnum<NloptLibrary.NloptResult> nloptResult = nloptAdapter.optimize(startPoint, objValue);
        System.out.println("nloptResult = " + nloptResult);
        System.out.println("object value = " + objValue[0]);
        System.out.println("expected object value = " + sqrt(8 / 27.0));
        assertEquals(sqrt(8 / 27.0), objValue[0], 1e-6);
    }

    @Test
    public void testSetterGetters() {
        final int dimension = 4;
        final NloptLibrary.NloptAlgorithm algorithm = NloptLibrary.NloptAlgorithm.NLOPT_LD_MMA;
        NloptAdapter nloptAdapter = new NloptAdapter(algorithm, dimension);
        assertEquals(dimension, nloptAdapter.getDimension());
        assertEquals(algorithm.value, nloptAdapter.getAlgorithm().value());
        double lowerBounds1 = 2.3;
        nloptAdapter.setLowerBounds(lowerBounds1);

        double[] exps = new double[dimension];
        Arrays.fill(exps, lowerBounds1);
        assertArrayEquals(exps, nloptAdapter.getLowerBounds(), 0);

        exps = new double[] { 1, 2, 3, 4, };
        nloptAdapter.setLowerBounds(exps);
        assertArrayEquals(exps, nloptAdapter.getLowerBounds(), 0);
        double[] pfexps = new double[dimension];
        Arrays.fill(pfexps, Double.POSITIVE_INFINITY);
        assertArrayEquals(pfexps, nloptAdapter.getUpperBounds(), 0);
        double[] upexps = new double[] { 1, 3, 4, 7 };
        nloptAdapter.setUpperBounds(exps);
        assertArrayEquals(exps, nloptAdapter.getUpperBounds(), 0);

        Arrays.fill(upexps, 12);
        nloptAdapter.setUpperBounds(upexps[0]);
        assertArrayEquals(upexps, nloptAdapter.getUpperBounds(), 0);

        assertArrayEquals(exps, nloptAdapter.getLowerBounds(), 0);

        double stopVal = 11.1;
        nloptAdapter.setStopValue(stopVal);
        assertEquals(stopVal, nloptAdapter.getStopValue(), 0);

        double fRelTol = -10;
        nloptAdapter.setRelativeFunctionTolerence(fRelTol);
        assertEquals(fRelTol, nloptAdapter.getRelativeFunctionTolerence(), 0);

        double fAbsTol = -11;
        nloptAdapter.setAbsoluteFunctionTolerence(fAbsTol);
        assertEquals(fAbsTol, nloptAdapter.getAbsoluteFunctionTolerence(), 0);

        // uncomplete
    }
}
