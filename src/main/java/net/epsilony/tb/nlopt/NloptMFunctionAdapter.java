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
