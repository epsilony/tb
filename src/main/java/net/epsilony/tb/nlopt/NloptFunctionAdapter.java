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
