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

package net.epsilony.tb.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class DifferentiableFunctionUtils {

    public static DifferentiableFunction max(
            Collection<? extends DifferentiableFunction> functions) {
        return new Max(new ArrayList<>(functions));
    }

    public static DifferentiableFunction min(
            Collection<? extends DifferentiableFunction> functions) {
        return new Min(new ArrayList<>(functions));
    }

    public static class Max extends AbstractMinMax {

        public Max(List<DifferentiableFunction> functions) {
            super(functions);
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (null == output) {
                output = new double[1];
            }
            double max = Double.NEGATIVE_INFINITY;
            for (DifferentiableFunction func : functions) {
                func.value(input, output);
                if (output[0] > max) {
                    max = output[0];
                }
            }
            output[0] = max;
            return output;
        }
    }

    public static class Min extends AbstractMinMax {

        public Min(List<DifferentiableFunction> functions) {
            super(functions);
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (null == output) {
                output = new double[1];
            }
            double min = Double.POSITIVE_INFINITY;
            for (DifferentiableFunction func : functions) {
                func.value(input, output);
                if (output[0] < min) {
                    min = output[0];
                }
            }
            output[0] = min;
            return output;
        }
    }

    private static abstract class AbstractMinMax implements DifferentiableFunction {

        public AbstractMinMax(List<DifferentiableFunction> functions) {
            this.functions = functions;
            if (null == functions || functions.isEmpty()) {
                throw new IllegalArgumentException();
            }

            Iterator<DifferentiableFunction> it = functions.iterator();
            DifferentiableFunction first = it.next();
            inputDimension = first.getInputDimension();
            outputDimension = first.getOutputDimension();
            while (it.hasNext()) {
                DifferentiableFunction func = it.next();
                if (inputDimension != func.getInputDimension() || outputDimension != func.getOutputDimension()) {
                    throw new IllegalArgumentException();
                }
            }
        }
        protected List<DifferentiableFunction> functions;
        protected int inputDimension;
        protected int outputDimension;

        @Override
        public int getDiffOrder() {
            return 0;
        }

        @Override
        public int getInputDimension() {
            return inputDimension;
        }

        @Override
        public int getOutputDimension() {
            return outputDimension;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            if (diffOrder != 0) {
                throw new IllegalArgumentException("only support 0, not " + diffOrder);
            }
        }

        @Override
        public abstract double[] value(double[] input, double[] output);
    }
}
