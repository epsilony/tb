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

package net.epsilony.tb.nlopt.demo;

import static net.epsilony.tb.nlopt.NloptLibrary.*;
import static java.lang.Math.*;
import net.epsilony.tb.nlopt.NloptFunctionAdapter;
import net.epsilony.tb.analysis.AbstractDifferentiableFunction;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NloptLibraryDemo {

    public static void main(String[] args) {
        NloptOpt opt = nloptCreate(NloptAlgorithm.NLOPT_LD_MMA, 2);
        Pointer<Byte> nloptAlgorithmName = nloptAlgorithmName(NloptAlgorithm.NLOPT_LD_MMA);
        System.out.println(nloptAlgorithmName.getCString());
        NloptFunc func = new NloptFunctionAdapter(new AbstractDifferentiableFunction() {
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
                output[0] = Math.sqrt(input[1]);
                output[1] = 0;
                output[2] = 0.5 / sqrt(input[1]);
                return output;
            }
        });

        NloptFunc constraint = new NloptFunc() {
            @Override
            public double apply(int n, Pointer<Double> x, Pointer<Double> gradient, Pointer<?> func_data) {
                Pointer<Double> myFuncData = (Pointer<Double>) func_data;
                double a = myFuncData.getDoubleAtIndex(0);
                double b = myFuncData.getDoubleAtIndex(1);
                double t = a * x.getDoubleAtIndex(0) + b;
                if (gradient != Pointer.NULL) {
                    gradient.setDoubleAtIndex(0, 3 * a * t * t);
                    gradient.setDoubleAtIndex(1, -1.0);
                }
                return (t * t * t - x.getDoubleAtIndex(1));
            }
        };
        Pointer<Double> lowerBounds = Pointer.pointerToDoubles(Double.NEGATIVE_INFINITY, 0);
        nloptSetLowerBounds(opt, lowerBounds);
        nloptSetMinObjective(opt, Pointer.pointerTo(func), Pointer.NULL);
        nloptAddInequalityConstraint(opt, Pointer.pointerTo(constraint), Pointer.pointerToDoubles(2, 0), 1e-8);
        nloptAddInequalityConstraint(opt, Pointer.pointerTo(constraint), Pointer.pointerToDoubles(-1, 1), 1e-8);
        nloptSetXtolRel(opt, 1e-4);

        Pointer<Double> start = Pointer.pointerToDoubles(1.234, 5.678);
        Pointer<Double> objValue = Pointer.pointerToDouble(0);
        IntValuedEnum<NloptResult> nloptOptimize = nloptOptimize(opt, start, objValue);
        System.out.println("nloptOptimize = " + nloptOptimize);
        System.out.println("objValue = " + objValue);
        System.out.println("expected objec value = " + sqrt(8 / 27.0));
        System.out.println(objValue.getDouble());
        nloptDestroy(opt);
    }
}
