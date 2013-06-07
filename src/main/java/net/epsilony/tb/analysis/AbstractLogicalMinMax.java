/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractLogicalMinMax implements DifferentiableFunction<double[], double[]> {

    DifferentiableFunction<double[], double[]> funA;
    DifferentiableFunction<double[], double[]> funB;
    LogicalHeaviside heaviside = new LogicalHeaviside();

    @Override
    public int getDiffOrder() {
        if (funA.getDiffOrder() != funB.getDiffOrder() || funA.getDiffOrder() != heaviside.getDiffOrder()) {
            throw new IllegalStateException("the upstream function is with different diff order, funA: " + funA.getDiffOrder() + " funB: " + funB.getDiffOrder());
        }
        return heaviside.getDiffOrder();
    }

    public double getErr(double x) {
        return heaviside.getErr(x);
    }

    public DifferentiableFunction<double[], double[]> getFunA() {
        return funA;
    }

    public DifferentiableFunction<double[], double[]> getFunB() {
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

    public void setFunctions(DifferentiableFunction<double[], double[]> funA, DifferentiableFunction<double[], double[]> funB) {
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
