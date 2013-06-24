/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.nlopt;

import net.epsilony.tb.analysis.DifferentiableFunction;
import static net.epsilony.tb.nlopt.NloptLibrary.*;
import org.bridj.Pointer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NloptFunctionAdapter extends NloptFunc {

    private final DifferentiableFunction function;
    private final double[] xs;
    private final double[] funcValue;

    @Override
    public double apply(int n, Pointer<Double> x, Pointer<Double> gradient, Pointer<?> func_data) {
        if (n != function.getInputDimension()) {
            throw new IllegalStateException();
        }
        
        x.getDoublesAtOffset(0, xs, 0, n);
        if (Pointer.NULL != gradient) {
            function.setDiffOrder(1);
        }
        function.value(xs, funcValue);
        double result = funcValue[0];
        if (Pointer.NULL != gradient) {
            gradient.setDoublesAtOffset(0, funcValue, 1, n);
        }
        return result;
    }

    public NloptFunctionAdapter(DifferentiableFunction function) {
        if (function.getOutputDimension() != 1) {
            throw new IllegalArgumentException("function should be real, not vector");
        }
        this.function = function;
        xs = new double[function.getInputDimension()];
        funcValue = new double[function.getInputDimension() + 1];
    }
}
