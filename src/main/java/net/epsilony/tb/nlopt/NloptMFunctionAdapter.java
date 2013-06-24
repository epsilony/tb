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
public class NloptMFunctionAdapter extends NloptMfunc {

    private final DifferentiableFunction function;
    private final double[] xs;
    private final double[] funcValues;

    @Override
    public void apply(int m, Pointer<Double> result,
            int n, Pointer<Double> x, Pointer<Double> gradient,
            Pointer<?> func_data) {
        if (function.getOutputDimension() != m) {
            throw new IllegalArgumentException("output dimension mismatch");
        }
        if (function.getInputDimension() != n) {
            throw new IllegalArgumentException("input dimension mismatch");
        }
        if (Pointer.NULL != gradient) {
            function.setDiffOrder(1);
        }

        x.getDoublesAtOffset(0, xs, 0, n);
        function.value(xs, funcValues);
        result.setDoublesAtOffset(0, funcValues, 0, m);
        if (gradient != Pointer.NULL) {
            gradient.setDoublesAtOffset(0, funcValues, m, m * n);
        }
    }

    public NloptMFunctionAdapter(DifferentiableFunction function) {
        this.function = function;
        xs = new double[function.getInputDimension()];
        funcValues = new double[(function.getInputDimension() + 1) * function.getOutputDimension()];
    }
}
