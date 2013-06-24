/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.DifferentiableFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface ImplicitFunctionSolver {

    double getFunctionAbsoluteTolerence();

    double[] getFunctionValue();

    int getMaxEval();

    double[] getSolution();

    double getSolutionAbsoluteTolerence();

    void setFunction(DifferentiableFunction function);

    void setFunctionAbsoluteTolerence(double functionAbsoluteTolerence);

    void setMaxEval(int maxEval);

    void setSolutionAbsoluteTolerence(double solutionAbsoluteTolerence);

    boolean solve(double[] start);
}
