/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LogicalMaximum extends AbstractLogicalMinMax {

    @Override
    public double[] value(double[] input, double[] output) {
        double[] vA = funA.value(input, null);
        double[] vB = funB.value(input, null);
        double fB = vB[0];
        double fA = vA[0];
        double vAmB = fA - fB;
        double[] vH = heaviside.values(vAmB, null);
        if (output == null) {
            output = new double[1 + getInputDimension()];
        }
        double fH = vH[0];
        output[0] = fH * fA + (1 - fH) * fB;
        if (getDiffOrder() >= 1) {
            double dH = vH[1];
            double dA = dH * (fA - fB) + fH;
            double dB = dH * (fB - fA) + 1 - fH;
            for (int i = 1; i <= getInputDimension(); i++) {
                output[i] = dA * vA[i] + dB * vB[i];
            }
        }
        return output;
    }
}
