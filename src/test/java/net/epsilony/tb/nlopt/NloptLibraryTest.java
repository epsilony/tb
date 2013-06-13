/* (c) Copyright by Man YUAN */
package net.epsilony.tb.nlopt;

import static java.lang.Math.sqrt;
import java.util.Arrays;
import net.epsilony.tb.analysis.AbstractDifferentiableFunction;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptAddInequalityConstraint;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptAlgorithmName;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptCreate;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptDestroy;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptOptimize;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptSetLowerBounds;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptSetMinObjective;
import static net.epsilony.tb.nlopt.NloptLibrary.nloptSetXtolRel;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class NloptLibraryTest {

    public NloptLibraryTest() {
    }

    @Test
    public void testOfTutorialExample() {
        NloptLibrary.NloptOpt opt = nloptCreate(NloptLibrary.NloptAlgorithm.NLOPT_LD_MMA, 2);
        Pointer<Byte> nloptAlgorithmName = nloptAlgorithmName(NloptLibrary.NloptAlgorithm.NLOPT_LD_MMA);
        System.out.println(nloptAlgorithmName.getCString());

        NloptLibrary.NloptFunc func = new NloptFunctionAdapter(new AbstractDifferentiableFunction() {
            int count = 0;

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
                System.out.println(count + ": " + Arrays.toString(input) + ", obj: " + output[0]);
                return output;
            }
        });

        NloptLibrary.NloptFunc constraint = new NloptLibrary.NloptFunc() {
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
        IntValuedEnum<NloptLibrary.NloptResult> nloptOptimize = nloptOptimize(opt, start, objValue);
        System.out.println("nloptOptimize = " + nloptOptimize);
        System.out.println("objValue = " + objValue);
        System.out.println(objValue.getDouble());
        nloptDestroy(opt);
        assertEquals(Math.sqrt(8 / 27d), objValue.getDouble(), 1e-6);
    }
}