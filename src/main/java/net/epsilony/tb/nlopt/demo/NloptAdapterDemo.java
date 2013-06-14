/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.nlopt.demo;

import static java.lang.Math.sqrt;
import java.util.Arrays;
import net.epsilony.tb.analysis.AbstractDifferentiableFunction;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.nlopt.NloptAdapter;
import net.epsilony.tb.nlopt.NloptLibrary;
import org.bridj.IntValuedEnum;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NloptAdapterDemo {

    static DifferentiableFunction<double[], double[]> objFunc = new AbstractDifferentiableFunction() {
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
            if (null == output) {
                output = new double[getDiffOrder() * 2 + 1];
            }
            output[0] = sqrt(input[1]);
            if (diffOrder >= 1) {
                output[1] = 0;
                output[2] = 0.5 / sqrt(input[1]);
            }
            System.out.println(count++ + ": " + Arrays.toString(input) + ", " + Arrays.toString(output));
            return output;
        }
    };
    static DifferentiableFunction<double[], double[]> constraints = new AbstractDifferentiableFunction() {
        double[][] abs = new double[][]{{2, 0}, {-1, 1}};
        int count;

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
                output = new double[getDiffOrder() * getOutputDimension() * getInputDimension() + getOutputDimension()];
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
            System.out.println("M" + count++ + ": " + Arrays.toString(input) + ", " + Arrays.toString(output));
            return output;
        }
    };
    static double[] constraintsTols = new double[]{1e-8, 1e-8};
    static double[] lowerBounds = new double[]{Double.NEGATIVE_INFINITY, 0};
    static double RelativeXTolerence = 1e-4;
    static double[] startPoint = new double[]{1.234, 5.768};

    public static void main(String[] args) {
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
    }
}
