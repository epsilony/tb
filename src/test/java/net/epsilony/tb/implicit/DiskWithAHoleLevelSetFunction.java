/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.Math2D;
import org.junit.Ignore;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
@Ignore
public class DiskWithAHoleLevelSetFunction implements DifferentiableFunction<double[], double[]> {

    int diffOrder = 0;
    double diskX = 50, diskY = 50, diskRad = 40;
    double holeX = 44, holeY = 42, holeRad = 15;

    @Override
    public double[] value(double[] input, double[] output) {
        double diskValue = diskRad - Math2D.distance(diskX, diskY, input[0], input[1]);
        double holeValue = Math2D.distance(holeX, holeY, input[0], input[1]) - holeRad;
        double value = Math.min(diskValue, holeValue);
        if (output == null) {
            return new double[]{value};
        } else {
            output[0] = value;
            return output;
        }
    }

    @Override
    public int getInputDimension() {
        return 2;
    }

    @Override
    public int getOutputDimension() {
        return 1;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder != 0) {
            throw new IllegalArgumentException("only support 0, not " + diffOrder);
        }
    }
}
