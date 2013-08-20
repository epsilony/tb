/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import gnu.trove.list.array.TDoubleArrayList;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.analysis.Dimensional;
import net.epsilony.tb.analysis.WithDiffOrder;
import net.epsilony.tb.analysis.WithDiffOrderUtil;
import net.epsilony.tb.synchron.SynchronizedClonable;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RadialBasis implements Dimensional, WithDiffOrder, SynchronizedClonable<RadialBasis> {

    RadialFunctionCore coreFunc;
    private int dim;

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

    public RadialBasis(RadialFunctionCore coreFunc) {
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
    public RadialBasis synchronizeClone() {
        RadialBasis result = new RadialBasis(coreFunc.synchronizeClone());
        return result;
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
