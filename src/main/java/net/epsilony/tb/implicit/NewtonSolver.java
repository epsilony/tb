/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import net.epsilony.tb.analysis.DifferentiableFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NewtonSolver {

    private double gradSq;

    public enum SolutionStatus {

        GOOD, DIVERGENT
    };
    public static double DEFAULT_ABSOLUTE_ERROR = 1E-6;
    public static int DEFAULT_MAX_EVAL = 10000;
    public static boolean DEFAULT_CHECK_FUNCTION_VALUE = false;
    DifferentiableFunction<double[], double[]> function;
    double absoluteError = DEFAULT_ABSOLUTE_ERROR;
    int maxEval = DEFAULT_MAX_EVAL;
    private double[] solution;
    private double solutionError;
    private double[] functionValue;
    private int funcInputDimension;
    int eval;
    SolutionStatus solutionStatus;
    private boolean checkFunctionValue = DEFAULT_CHECK_FUNCTION_VALUE;

    private boolean isCheckFunctionValue() {
        return checkFunctionValue;
    }

    public DifferentiableFunction<double[], double[]> getFunction() {
        return function;
    }

    public void setFunction(DifferentiableFunction<double[], double[]> function) {

        if (1 != function.getOutputDimension()) {
            throw new IllegalArgumentException();
        }

        this.function = function;
        funcInputDimension = function.getInputDimension();
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public void setAbsoluteError(double absoluteError) {
        this.absoluteError = absoluteError;
    }

    public int getMaxEval() {
        return maxEval;
    }

    public void setMaxEval(int maxEval) {
        this.maxEval = maxEval;
    }

    public boolean solve(double[] start) {
        solution = Arrays.copyOf(start, start.length);
        eval = 0;
        do {
            iterate();
            eval++;
        } while (keepIteration());
        if (solutionStatus == SolutionStatus.GOOD) {
            return true;
        } else {
            return false;
        }
    }

    public double[] getSolution() {
        return solution;
    }

    public SolutionStatus getSolutionStatus() {
        return solutionStatus;
    }

    private void iterate() {
        functionValue = function.value(solution, functionValue);

        solutionError = 0;
        if (functionValue[0] == 0) {
            return;
        }
        calcGradientSq();
        if (0 == gradSq) {
            return;
        }
        double value = -functionValue[0];
        for (int i = 0; i < funcInputDimension; i++) {
            double d = value / gradSq * functionValue[i + 1];
            solutionError += d * d;
            solution[i] += d;
        }
        solutionError = Math.sqrt(solutionError);
    }

    public double[] getFunctionValue() {
        return functionValue;
    }

    public int getEvalTimes() {
        return eval;
    }

    public double getSolutionError() {
        return solutionError;
    }

    private double calcGradientSq() {
        gradSq = 0;
        for (int i = 1; i <= funcInputDimension; i++) {
            double v = functionValue[i];
            gradSq += v * v;
        }
        return gradSq;
    }

    private boolean keepIteration() {
        if (gradSq == 0) {
            if (Math.abs(functionValue[0]) <= absoluteError) {
                solutionStatus = SolutionStatus.GOOD;
            } else {
                solutionStatus = SolutionStatus.DIVERGENT;
            }
            return false;
        }
        if (solutionError <= absoluteError) {
            if (isCheckFunctionValue() && Math.abs(functionValue[0]) > absoluteError && eval < maxEval) {
                return true;
            }
            solutionStatus = SolutionStatus.GOOD;
            return false;
        }
        if (eval >= maxEval) {
            solutionStatus = SolutionStatus.DIVERGENT;
            return false;
        }
        return true;
    }
}
