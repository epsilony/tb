/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LogicalMinimumTest {

    public LogicalMinimumTest() {
    }

    public static class FunA_1D implements DifferentiableFunction {

        @Override
        public int getInputDimension() {
            return 1;
        }

        @Override
        public int getOutputDimension() {
            return 1;
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (output == null) {
                output = new double[2];
            }
            double x = input[0];
            output[0] = (x - 1.5) * (x - 1.5) - 1;
            output[1] = 2 * (x - 1.5);
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 1;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
        }
    }

    public static class FunB_1D implements DifferentiableFunction {

        @Override
        public int getInputDimension() {
            return 1;
        }

        @Override
        public int getOutputDimension() {
            return 1;
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (output == null) {
                output = new double[2];
            }
            double x = input[0];
            output[0] = (x + 1.5) * (x + 1.5) - 1;
            output[1] = 2 * (x + 1.5);
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 1;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
        }
    }

    @Test
    public void test1D() {
        LogicalMinimum lm = new LogicalMinimum();
        lm.setK(1.5, 1e-4, true);//k=4

        lm.setFunctions(new FunA_1D(), new FunB_1D());
        lm.setDiffOrder(1);
        double[][] x_exp_errs = new double[][]{
            {0, 1.5 * 1.5 - 1, 0, 1e-14},
            {1.5, -1, 0, 1e-8},
            {0.1, 0.964897542691896, -2.98418774833853, 1e-14},
            {-0.05, 1.12745180894818, 3.49903580738314, 1e-14}
        };
        for (double[] x_exp_err : x_exp_errs) {
            double x = x_exp_err[0];
            double[] acts = lm.value(new double[]{x}, null);
            double[] exps = Arrays.copyOfRange(x_exp_err, 1, x_exp_err.length - 1);
            double err = x_exp_err[x_exp_err.length - 1];
            assertArrayEquals(exps, acts, err);
        }

        double x = 10;//a far point
        FunA_1D funcA = new FunA_1D();
        funcA.setDiffOrder(1);
        double[] exps = funcA.value(new double[]{x}, null);
        double[] acts = lm.value(new double[]{x}, null);
        assertArrayEquals(exps, acts, 1e-14);
    }

    public static class FunA_2D implements DifferentiableFunction {

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
            if (output == null) {
                output = new double[3];
            }
            double x = input[0];
            double y = input[1];
            output[0] = (x + 1.5) * (x + 1.5) + y * y - 1;
            output[1] = 2 * (x + 1.5);
            output[2] = 2 * y;
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 1;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
        }
    }

    public static class FunB_2D implements DifferentiableFunction {

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
            if (output == null) {
                output = new double[3];
            }
            double x = input[0];
            double y = input[1];
            output[0] = (x - 2) * (x - 2) + y * y - 1;
            output[1] = 2 * (x - 2);
            output[2] = 2 * y;
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 1;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
        }
    }

    @Test
    public void test2D() {
        LogicalMinimum lm = new LogicalMinimum();
        lm.setK(1.5, 1e-4, true);  //k=4;

        lm.setFunctions(new FunA_2D(), new FunB_2D());
        lm.setDiffOrder(1);
        double[][] x_exp_errs = new double[][]{
            {0.25, 0, 1.75 * 1.75 - 1, 0, 0, 1e-14},
            {0.3, -0.7, 2.40006346156460, -4.05787781793305, -1.40000000000000, 1e-14},
            {-0.22, 0.07, 0.643300000012206, 2.56000000065755, 0.140000000000000, 1e-14}
        };
        for (double[] x_exp_err : x_exp_errs) {
            double[] x = Arrays.copyOfRange(x_exp_err, 0, 2);
            double[] acts = lm.value(x, null);
            double[] exps = Arrays.copyOfRange(x_exp_err, 2, x_exp_err.length - 1);
            double err = x_exp_err[x_exp_err.length - 1];
            assertArrayEquals(exps, acts, err);
        }
    }
}