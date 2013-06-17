/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.nlopt.NloptAdapter;
import net.epsilony.tb.nlopt.NloptLibrary;
import static net.epsilony.tb.nlopt.NloptLibrary.*;
import org.bridj.IntValuedEnum;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MMAFunctionSolver implements ConstraintImplicitFunctionSolver {

    public static double DEFAULT_ABSOLUTE_FUNCTION_TOLERENCE = 1E-4;
    NloptAdapter nloptAdapter;
    double[] x;
    double[] solution;
    double[] oriValue;
    DifferentiableFunction<double[], double[]> oriFunction;
    DifferentiableFunction<double[], double[]> squaredFunction = new SquaredFunction();

    private class SquaredFunction implements DifferentiableFunction<double[], double[]> {

        @Override
        public int getInputDimension() {
            return oriFunction.getInputDimension();
        }

        @Override
        public int getOutputDimension() {
            return oriFunction.getOutputDimension();
        }

        @Override
        public int getDiffOrder() {
            return oriFunction.getDiffOrder();
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            oriFunction.setDiffOrder(diffOrder);
        }

        @Override
        public double[] value(double[] input, double[] output) {
            oriValue = oriFunction.value(input, oriValue);
            double t = oriValue[0] * 2;
            for (int i = 1; i < oriValue.length; i++) {
                output[i] = t * oriValue[i];
            }
            output[0] = oriValue[0] * oriValue[0];
            return output;
        }
    }

    public MMAFunctionSolver(int dimension) {
        nloptAdapter = new NloptAdapter(NloptAlgorithm.NLOPT_LD_MMA, dimension);
        _setFunctionAbsoluteTolerence(DEFAULT_ABSOLUTE_FUNCTION_TOLERENCE);
    }

    @Override
    public double getFunctionAbsoluteTolerence() {
        return Math.sqrt(nloptAdapter.getAbsoluteFunctionTolerence());
    }

    @Override
    public double[] getFunctionValue() {
        return oriValue;
    }

    @Override
    public int getMaxEval() {
        return nloptAdapter.getMaxEval();
    }

    @Override
    public double[] getSolution() {
        return nloptAdapter.getSolution();
    }

    @Override
    public double getSolutionAbsoluteTolerence() {
        return Math.sqrt(nloptAdapter.getStopValue());
    }

    @Override
    public void setFunction(DifferentiableFunction<double[], double[]> function) {
        oriFunction = function;
        nloptAdapter.setMinObjective(squaredFunction);
    }

    @Override
    public void setFunctionAbsoluteTolerence(double functionAbsoluteTolerence) {
        _setFunctionAbsoluteTolerence(functionAbsoluteTolerence);
    }

    private void _setFunctionAbsoluteTolerence(double functionAbsoluteTolerence) {
        double tol = functionAbsoluteTolerence * functionAbsoluteTolerence;
        if (functionAbsoluteTolerence < 0) {
            tol = -tol;

        }
        nloptAdapter.setStopValue(tol);
    }

    @Override
    public void setMaxEval(int maxEval) {
        nloptAdapter.setMaxEval(maxEval);
    }

    @Override
    public void setSolutionAbsoluteTolerence(double solutionAbsoluteTolerence) {
        nloptAdapter.setAbsoluteXTolerence(solutionAbsoluteTolerence);
    }

    @Override
    public boolean solve(double[] start) {
        oriValue = null;
        double[] startCopy = Arrays.copyOf(start, start.length);
        IntValuedEnum<NloptLibrary.NloptResult> nloptResult = nloptAdapter.optimize(startCopy, null);
        if (nloptResult.value() > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setLowerBounds(double[] lowerBounds) {
        nloptAdapter.setLowerBounds(lowerBounds);
    }

    @Override
    public void setUpperBounds(double[] upperBounds) {
        nloptAdapter.setUpperBounds(upperBounds);
    }

    @Override
    public void addConstraint(DifferentiableFunction<double[], double[]> function, double tolerence) {
        nloptAdapter.addInequalityConstraint(function, tolerence);
    }
}
