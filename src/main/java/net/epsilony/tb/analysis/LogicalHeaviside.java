/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LogicalHeaviside implements UnivarDifferentiableDoubleFunction, UnivarArrayFunction {

    double k = 8;
    int diffOrder;

    @Override
    public double value(double x) {
        return 1 / (1 + Math.exp(-2 * k * x));
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("Only support 0 or 1, not " + diffOrder);
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public double[] values(double x, double[] results) {
        if (null == results) {
            results = new double[diffOrder + 1];
        }
        double ev = Math.exp(-2 * k * x);
        results[0] = 1 / (1 + ev);
        if (diffOrder >= 1) {
            double t = 1 + ev;
            t *= t;
            results[1] = 2 * k * ev / t;
        }
        return results;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setK(double x, double err) {
        if (x == 0) {
            throw new IllegalArgumentException("x cannot be 0!");
        }
        if (err <= 0 || err >= 0.5) {
            throw new IllegalArgumentException("err should be in (0,0.5), not " + err);
        }
        k = Math.log(1 / err - 1) / 2 / Math.abs(x);
    }

    public double getErr(double x) {
        if (0 == x) {
            return 0;
        }
        if (x > 0) {
            return 1 - value(x);
        } else {
            return value(x);
        }
    }
}
