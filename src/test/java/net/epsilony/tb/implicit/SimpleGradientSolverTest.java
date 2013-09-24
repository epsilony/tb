/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.DifferentiableFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SimpleGradientSolverTest {

    public SimpleGradientSolverTest() {
    }

    public static class Quadric1D implements DifferentiableFunction {

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
                output = new double[2];
            }
            double x = input[0];
            output[0] = x * x;
            output[1] = 2 * x;
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

    //@Test
    public void test1DQuadric() {
        SimpleGradientSolver solver = new SimpleGradientSolver();
        solver.setFunction(new Quadric1D());
        solver.solve(new double[]{1000});
        assertEquals(0, solver.getSolution()[0], 1e-6);
        assertTrue(solver.getSolutionStatus() == SimpleGradientSolver.SolutionStatus.GOOD);
        assertTrue(solver.getEvalTimes() <= 30);
        assertTrue(Math.abs(solver.getFunctionValue()[0]) < 1e-6);

        solver.solve(new double[]{0});
    }

    public static class Quadric2D implements DifferentiableFunction {

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
        SimpleGradientSolver solver = new SimpleGradientSolver();
        solver.setFunction(new Quadric2D());
        solver.solve(new double[]{1000, 1000});
        solver.setMaxEval(50);
        assertEquals(1.5, solver.getSolution()[0], 1e-6);
        assertEquals(-2.3, solver.getSolution()[1], 1e-6);
        assertTrue(solver.getSolutionStatus() == SimpleGradientSolver.SolutionStatus.GOOD);
        assertTrue(Math.abs(solver.getFunctionValue()[0]) < 1e-6);
    }

    public static class Quadric1DLocalMini implements DifferentiableFunction {

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
                output = new double[2];
            }
            double x = input[0];
            output[0] = x * x + 1;
            output[1] = 2 * x;
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
    public void testQuadric1DLocalMini() {
        SimpleGradientSolver solver = new SimpleGradientSolver();
        solver.setFunction(new Quadric1DLocalMini());
        solver.solve(new double[]{0.1});
        solver.setMaxEval(50);
        assertEquals(SimpleGradientSolver.SolutionStatus.DIVERGENT, solver.getSolutionStatus());
        solver.solve(new double[]{0});
        assertEquals(SimpleGradientSolver.SolutionStatus.DIVERGENT, solver.getSolutionStatus());
    }
}
