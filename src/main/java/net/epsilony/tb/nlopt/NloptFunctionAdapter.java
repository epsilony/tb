/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.nlopt;

import net.epsilony.tb.analysis.DifferentiableFunction;
import org.bridj.Pointer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NloptFunctionAdapter extends NloptLibrary.NloptFunc {

    DifferentiableFunction<double[], double[]> function;
    double[] xs;
    double[] results;

    @Override
    public double apply(int n, Pointer<Double> x, Pointer<Double> gradient, Pointer<?> func_data) {
        if (n != function.getInputDimension()) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < xs.length; i++) {
            xs[i] = x.getDoubleAtIndex(i);
        }
        if (null != gradient) {
            function.setDiffOrder(1);
        }
        function.value(xs, results);
        double result = results[0];
        if (null != gradient) {
            gradient.setDoublesAtOffset(0, results, 1, n);
        }
        return result;
    }

    public NloptFunctionAdapter(DifferentiableFunction<double[], double[]> function) {
        if (function.getOutputDimension() != 1) {
            throw new IllegalArgumentException("function should be real, not vector");
        }
        this.function = function;
        xs = new double[function.getInputDimension()];
        results = new double[function.getInputDimension() + 1];
    }
}
