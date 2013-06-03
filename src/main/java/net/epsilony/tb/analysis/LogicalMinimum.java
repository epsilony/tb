/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LogicalMinimum implements DifferentiableFunction<double[], double[]> {

    LogicalHeaviside heaviside = new LogicalHeaviside();
    DifferentiableFunction<double[], double[]> funA, funB;

    public DifferentiableFunction<double[], double[]> getFunA() {
        return funA;
    }

    public DifferentiableFunction<double[], double[]> getFunB() {
        return funB;
    }

    public void setFunctions(
            DifferentiableFunction<double[], double[]> funA,
            DifferentiableFunction<double[], double[]> funB) {
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

    @Override
    public int getInputDimension() {
        return funA.getInputDimension();
    }

    @Override
    public int getOutputDimension() {
        return 1;
    }

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
        output[0] = fH * fB + (1 - fH) * fA;
        if (getDiffOrder() >= 1) {
            double dH = vH[1];
            double dA = dH * (fB - fA) + 1 - fH;
            double dB = dH * (fA - fB) + fH;
            for (int i = 1; i <= getInputDimension(); i++) {
                output[i] = dA * vA[i] + dB * vB[i];
            }
        }
        return output;
    }

    @Override
    public int getDiffOrder() {
        if (funA.getDiffOrder() != funB.getDiffOrder()
                || funA.getDiffOrder() != heaviside.getDiffOrder()) {
            throw new IllegalStateException("the upstream function is with different diff order, funA: "
                    + funA.getDiffOrder()
                    + " funB: " + funB.getDiffOrder());
        }
        return heaviside.getDiffOrder();
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

    public double getK() {
        return heaviside.getK();
    }

    public void setK(double k) {
        heaviside.setK(k);
    }

    public void setK(double x, double err) {
        heaviside.setK(x, err);
    }

    public double getErr(double x) {
        return heaviside.getErr(x);
    }
}
