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

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractLogicalMinMax implements DifferentiableFunction {

    DifferentiableFunction funA;
    DifferentiableFunction funB;
    LogicalHeaviside heaviside = new LogicalHeaviside();

    @Override
    public int getDiffOrder() {
        if (funA.getDiffOrder() != funB.getDiffOrder() || funA.getDiffOrder() != heaviside.getDiffOrder()) {
            throw new IllegalStateException("the upstream function is with different diff order, funA: "
                    + funA.getDiffOrder() + " funB: " + funB.getDiffOrder());
        }
        return heaviside.getDiffOrder();
    }

    public double getErr(double x) {
        return heaviside.getErr(x);
    }

    public DifferentiableFunction getFunA() {
        return funA;
    }

    public DifferentiableFunction getFunB() {
        return funB;
    }

    @Override
    public int getInputDimension() {
        return funA.getInputDimension();
    }

    public double getK() {
        return heaviside.getK();
    }

    @Override
    public int getOutputDimension() {
        return 1;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("Only support diffOrder 0 or 1, not " + diffOrder);
        }
        funA.setDiffOrder(diffOrder);
        funB.setDiffOrder(diffOrder);
        heaviside.setDiffOrder(diffOrder);
    }

    public void setFunctions(DifferentiableFunction funA, DifferentiableFunction funB) {
        if (funA.getOutputDimension() != 1 || funB.getOutputDimension() != 1) {
            throw new IllegalArgumentException("Only support 1D output function");
        }
        if (funA.getInputDimension() != funB.getInputDimension()) {
            throw new IllegalArgumentException("functions should have same input dimensions");
        }
        this.funA = funA;
        this.funB = funB;
        setDiffOrder(0);
    }

    public void setK(double k) {
        heaviside.setK(k);
    }

    public void setK(double x, double err) {
        heaviside.setK(x, err);
    }

    public void setK(double x, double err, boolean ceilRound) {
        heaviside.setK(x, err, ceilRound);
    }
}
