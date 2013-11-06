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

package net.epsilony.tb.implicit;

import java.util.Arrays;
import java.util.Random;
import net.epsilony.tb.analysis.DifferentiableFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SimpleGradientSolver implements ImplicitFunctionSolver {

    private double gradSq;

    public enum SolutionStatus {

        GOOD, DIVERGENT
    };
    public static double DEFAULT_SOLUTION_ABSOLUTE_TOLERENCE = 1E-6;
    public static int DEFAULT_MAX_EVAL = 10000;
    public static double DEFAULT_ABSOLUTE_FUNCTION_TOLERENCE = -1;
    DifferentiableFunction function;
    double solutionAbsoluteTolerence = DEFAULT_SOLUTION_ABSOLUTE_TOLERENCE;
    int maxEval = DEFAULT_MAX_EVAL;
    private double[] solution;
    private double solutionStep;
    private double[] functionValue;
    private int funcInputDimension;
    int eval;
    SolutionStatus solutionStatus;
    private double functionAbsoluteTolerence = DEFAULT_ABSOLUTE_FUNCTION_TOLERENCE;

    public DifferentiableFunction getFunction() {
        return function;
    }

    @Override
    public void setFunction(DifferentiableFunction function) {

        if (1 != function.getOutputDimension()) {
            throw new IllegalArgumentException();
        }

        this.function = function;
        funcInputDimension = function.getInputDimension();
    }

    @Override
    public double getSolutionAbsoluteTolerence() {
        return solutionAbsoluteTolerence;
    }

    @Override
    public void setSolutionAbsoluteTolerence(double solutionAbsoluteTolerence) {
        this.solutionAbsoluteTolerence = solutionAbsoluteTolerence;
    }

    @Override
    public int getMaxEval() {
        return maxEval;
    }

    @Override
    public void setMaxEval(int maxEval) {
        this.maxEval = maxEval;
    }

    @Override
    public boolean solve(double[] start) {
        function.setDiffOrder(1);
        functionValue = null;
        solutionStatus = SolutionStatus.DIVERGENT;
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

    @Override
    public double[] getSolution() {
        return solution;
    }

    public SolutionStatus getSolutionStatus() {
        return solutionStatus;
    }

    private void iterate() {
        functionValue = function.value(solution, functionValue);

        solutionStep = 0;
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
            solutionStep += d * d;
            solution[i] += d;
        }
        solutionStep = Math.sqrt(solutionStep);
    }

    @Override
    public double[] getFunctionValue() {
        return functionValue;
    }

    public int getEvalTimes() {
        return eval;
    }

    public double getSolutionError() {
        return solutionStep;
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
            if (Math.abs(functionValue[0]) <= solutionAbsoluteTolerence) {
                solutionStatus = SolutionStatus.GOOD;
            } else {
                if (eval < maxEval) {
                    Random rand = new Random();
                    for (int i = 0; i < solution.length; i++) {
                        solution[i] += rand.nextDouble() * 4 - 2 * solutionAbsoluteTolerence;
                    }
                    return true;
                }
                solutionStatus = SolutionStatus.DIVERGENT;
            }
            return false;
        }
        if (solutionStep <= solutionAbsoluteTolerence) {
            if (Math.abs(functionValue[0]) > functionAbsoluteTolerence && eval < maxEval) {
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

    @Override
    public double getFunctionAbsoluteTolerence() {
        return functionAbsoluteTolerence;
    }

    @Override
    public void setFunctionAbsoluteTolerence(double functionAbsoluteTolerence) {
        this.functionAbsoluteTolerence = functionAbsoluteTolerence;
    }
}
