/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.DifferentiableFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface ImplicitFunctionSolver {

    int getEvalTimes();

    DifferentiableFunction<double[], double[]> getFunction();

    double getFunctionAbsoluteTolerence();

    double[] getFunctionValue();

    int getMaxEval();

    double[] getSolution();

    double getSolutionAbsoluteTolerence();

    double getSolutionError();

    void setFunction(DifferentiableFunction<double[], double[]> function);

    void setFunctionAbsoluteTolerence(double functionAbsoluteTolerence);

    void setMaxEval(int maxEval);

    void setSolutionAbsoluteTolerence(double solutionAbsoluteTolerence);

    boolean solve(double[] start);
    
}