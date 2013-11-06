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

package net.epsilony.tb.common_func;

import gnu.trove.list.array.TDoubleArrayList;
import java.io.Serializable;
import net.epsilony.tb.IntIdentity;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.analysis.Dimensional;
import net.epsilony.tb.analysis.WithDiffOrder;
import net.epsilony.tb.analysis.WithDiffOrderUtil;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RadialBasis implements IntIdentity, Dimensional, WithDiffOrder, Serializable {

    RadialBasisCore coreFunc;
    private int dim;
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public RadialBasisCore getCoreFunc() {
        return coreFunc;
    }

    public void setCoreFunc(RadialBasisCore coreFunc) {
        this.coreFunc = coreFunc;
    }

    @Override
    public int getDiffOrder() {
        return coreFunc.getDiffOrder();
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("diffOrder should only be 0 or 1, not " + diffOrder);
        }
        coreFunc.setDiffOrder(diffOrder);
    }

    public TDoubleArrayList[] initOutput(int capacity, TDoubleArrayList[] outputs) {
        return WithDiffOrderUtil.initOutput(outputs, capacity, 2, getDiffOrder());
    }

    public RadialBasis(RadialBasisCore coreFunc) {
        this.coreFunc = coreFunc;
    }

    public RadialBasis() {
        this.coreFunc = new TripleSpline();
    }

    public double[] values(double[] dists, double influenceRad, double[] output) {
        int outputLength = WithDiffOrderUtil.outputLength(dim, getDiffOrder());
        if (null == output) {
            output = new double[outputLength];
        }
        coreFunc.valuesByDistance(dists[0] / influenceRad, output);
        if (getDiffOrder() >= 1) {
            double d = output[1];
            double t = d != 0 ? d / influenceRad : 0;
            if (Double.isNaN(t)) {
                throw new IllegalStateException();
            }
            for (int j = 1; j < outputLength; j++) {
                output[j] = t * dists[j];
            }
        }
        return output;
    }

    @Override
    public String toString() {
        return MiscellaneousUtils.simpleToString(this) + '{' + "coreFunc=" + coreFunc + '}';
    }

    @Override
    public void setDimension(int dim) {
        if (dim < 1 || dim > 3) {
            throw new IllegalArgumentException("dimension should only be 1-3, not " + dim);
        }
        this.dim = dim;
    }

    @Override
    public int getDimension() {
        return dim;
    }

    public static void main(String[] args) {
        System.out.println("1.0/0.0 = " + 0.0 / 0.0);
    }
}
