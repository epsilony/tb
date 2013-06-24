/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import net.epsilony.tb.analysis.AbstractDifferentiableFunction;
import net.epsilony.tb.analysis.DifferentiableFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MMAFunctionSolverTest {

    public MMAFunctionSolverTest() {
    }

    public static class Linear1D extends AbstractDifferentiableFunction {

        final static double k = 3;
        final static double x0 = 10;
        final static double y0 = -3;
        int count = 0;

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
            if (null == output) {
                output = new double[1 + diffOrder];
            }
            double x = input[0];
            double y = k * (x - x0) + y0;
            output[0] = y;
            if (diffOrder >= 1) {
                output[1] = k;
            }
            System.out.println("evalTime = " + ++count);
            return output;
        }
    }

    @Test
    public void testLinear1D() {
        double tol = 1e-3;
        MMAFunctionSolver solver = new MMAFunctionSolver(1);
        DifferentiableFunction func = new Linear1D();
        solver.setFunction(func);
        solver.setMaxEval(200);
        solver.setFunctionAbsoluteTolerence(tol);
        solver.solve(new double[]{1});
        double[] functionValue = solver.getFunctionValue();
        double[] solution = solver.getSolution();
        final double exp = -Linear1D.y0 / Linear1D.k + Linear1D.x0;
        assertEquals(exp, solution[0], tol);
        assertArrayEquals(functionValue, func.value(new double[]{exp}, null), tol);
    }

    public static class Quadric2D implements DifferentiableFunction {

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
            if (output == null) {
                output = new double[3];
            }
            double x = input[0];
            double y = input[1];

            double value = (x - 1.5) * (x - 1.5) / 4 + (y + 2.3) * (y + 2.3);
            double dx = (x - 1.5) / 2;
            double dy = (y + 2.3) * 2;
            output[0] = value;
            output[1] = dx;
            output[2] = dy;
            System.out.println(++count + "in: " + Arrays.toString(input) + ", out: " + Arrays.toString(output));
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 1;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            if (diffOrder != 1) {
                throw new IllegalArgumentException("Only support 1 for testing");
            }
        }
    }

    @Test
    public void test2DQuadric() {
        MMAFunctionSolver solver = new MMAFunctionSolver(2);
        solver.setFunction(new Quadric2D());
        solver.setMaxEval(200);
        final double tol = 1e-6;
        solver.setFunctionAbsoluteTolerence(-1);
        solver.setSolutionAbsoluteTolerence(tol);
        assertTrue(solver.solve(new double[]{1000, 1000}));
        assertEquals(1.5, solver.getSolution()[0], tol * 10);
        assertEquals(-2.3, solver.getSolution()[1], tol * 10);
        assertTrue(Math.abs(solver.getFunctionValue()[0]) < tol);
        
        assertTrue(solver.solve(new double[]{20, -30}));
        assertEquals(1.5, solver.getSolution()[0], tol * 10);
        assertEquals(-2.3, solver.getSolution()[1], tol * 10);
        assertTrue(Math.abs(solver.getFunctionValue()[0]) < tol);
    }
}