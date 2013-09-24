/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import net.epsilony.tb.analysis.WithDiffOrderUtil;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MonomialBases implements BasesFunction {

    int id;
    int diffOrder = 0;
    int dim = 2;
    int degree = 2;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("diffOrder must be 0 or 1, not " + diffOrder);
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public int getDimension() {
        return dim;
    }

    @Override
    public void setDimension(int dim) {
        if (dim < 1 || dim > 3) {
            throw new IllegalArgumentException("dim should be 1-3, not " + dim);
        }
        this.dim = dim;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        if (degree < 1 || degree > 3) {
            throw new IllegalArgumentException("only supports degree 1-3, not " + degree);
        }
        this.degree = degree;
    }

    @Override
    public double[][] values(double[] vec, double[][] results) {
        switch (dim) {
            case 1:
                return monomials1D(vec, degree, diffOrder, results);
            case 2:
                return monomials2D(vec, degree, diffOrder, results);
            case 3:
                return monomials3D(vec, degree, diffOrder, results);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int basesSize() {
        return WithDiffOrderUtil.outputLength(dim, degree);
    }

    public static double[][] monomials1D(double[] vec, int degree, int diffOrder, double[][] result) {
        if (degree > 3 || degree < 1) {
            throw new IllegalArgumentException("only supports degrees 1-3, not " + degree);
        }
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("diffOrder is not 0-1, but " + diffOrder);
        }

        if (null == result) {
            result = new double[WithDiffOrderUtil.outputLength(1, diffOrder)][WithDiffOrderUtil.outputLength(1, degree)];
        }

        double[] ori = result[0];
        double[] diffed = diffOrder >= 1 ? result[1] : null;
        double x = vec[0];
        switch (degree) {
            case 1:
                ori[0] = 1;
                ori[1] = x;
                if (diffOrder >= 1) {
                    diffed[0] = 0;
                    diffed[1] = 1;
                }
                break;
            case 2:
                ori[0] = 1;
                ori[1] = x;
                ori[2] = x * x;
                if (diffOrder >= 1) {
                    diffed[0] = 0;
                    diffed[1] = 1;
                    diffed[2] = 2 * x;
                }
                break;
            case 3:
                ori[0] = 1;
                ori[1] = x;
                double x2 = x * x;
                ori[2] = x2;
                ori[3] = x * x2;
                if (diffOrder >= 1) {
                    diffed[0] = 0;
                    diffed[1] = 1;
                    diffed[2] = 2 * x;
                    diffed[3] = 3 * x2;
                }
                break;
            default:
                throw new IllegalStateException();
        }
        return result;
    }

    public static double[][] monomials2D(double[] vec, int degree, int diffOrder, double[][] results) {
        if (degree > 3 || degree < 1) {
            throw new IllegalArgumentException("only supports degrees 1-3, not " + degree);
        }
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("diffOrder is not 0-1, but " + diffOrder);
        }

        if (null == results) {
            results = new double[WithDiffOrderUtil.outputLength(2, diffOrder)][WithDiffOrderUtil.outputLength(2, degree)];
        }

        double[] res = results[0];
        double[] res_x = null, res_y = null;
        if (diffOrder >= 1) {
            res_x = results[1];
            res_y = results[2];
        }
        double x = vec[0], y = vec[1];
        switch (degree) {
            case 1:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                }
                break;
            case 2:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                res[3] = x * x;
                res[4] = x * y;
                res[5] = y * y;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;
                    res_x[3] = 2 * x;
                    res_x[4] = y;
                    res_x[5] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                    res_y[3] = 0;
                    res_y[4] = x;
                    res_y[5] = 2 * y;
                }
                break;
            case 3:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                double x2 = x * x;
                res[3] = x2;
                double xy = x * y;
                res[4] = xy;
                double y2 = y * y;
                res[5] = y2;
                res[6] = x2 * x;
                res[7] = x2 * y;
                res[8] = x * y2;
                res[9] = y * y2;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;
                    res_x[3] = 2 * x;
                    res_x[4] = y;
                    res_x[5] = 0;
                    res_x[6] = 3 * x2;
                    res_x[7] = 2 * xy;
                    res_x[8] = y2;
                    res_x[9] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                    res_y[3] = 0;
                    res_y[4] = x;
                    res_y[5] = 2 * y;
                    res_y[6] = 0;
                    res_y[7] = x2;
                    res_y[8] = 2 * xy;
                    res_y[9] = 3 * y2;
                }
                break;
            default:
                throw new IllegalStateException();
        }
        return results;
    }

    public static double[][] monomials3D(double[] vec, int degree, int diffOrder, double[][] results) {
        if (degree > 3 || degree < 1) {
            throw new IllegalArgumentException("only supports degrees 1-3, not " + degree);
        }
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("diffOrder is not 0-1, but " + diffOrder);
        }

        if (null == results) {
            results = new double[WithDiffOrderUtil.outputLength(3, diffOrder)][WithDiffOrderUtil.outputLength(3, degree)];
        }

        double[] res = results[0];
        double[] res_x = null, res_y = null, res_z = null;
        if (diffOrder >= 1) {
            res_x = results[1];
            res_y = results[2];
            res_z = results[3];
        }
        double x = vec[0], y = vec[1], z = vec[2];
        double xy = x * y;
        switch (degree) {
            case 1:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                res[3] = z;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;
                    res_x[3] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                    res_y[3] = 0;

                    res_z[0] = 0;
                    res_z[1] = 0;
                    res_z[2] = 0;
                    res_z[3] = 1;
                }
                break;
            case 2:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                res[3] = z;
                res[4] = x * x;
                res[5] = xy;
                res[6] = y * y;
                res[7] = x * z;
                res[8] = y * z;
                res[9] = z * z;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;
                    res_x[3] = 0;
                    res_x[4] = 2 * x;
                    res_x[5] = y;
                    res_x[6] = 0;
                    res_x[7] = z;
                    res_x[8] = 0;
                    res_x[9] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                    res_y[3] = 0;
                    res_y[4] = 0;
                    res_y[5] = x;
                    res_y[6] = 2 * y;
                    res_y[7] = 0;
                    res_y[8] = z;
                    res_y[9] = 0;

                    res_z[0] = 0;
                    res_z[1] = 0;
                    res_z[2] = 0;
                    res_z[3] = 1;
                    res_z[4] = 0;
                    res_z[5] = 0;
                    res_z[6] = 0;
                    res_z[7] = x;
                    res_z[8] = y;
                    res_z[9] = 2 * z;
                }
                break;
            case 3:
                res[0] = 1;
                res[1] = x;
                res[2] = y;
                res[3] = z;
                double x2 = x * x;
                res[4] = x2;
                res[5] = xy;
                double y2 = y * y;
                res[6] = y2;
                double xz = x * z;
                res[7] = xz;
                double yz = y * z;
                res[8] = yz;
                double z2 = z * z;
                res[9] = z2;
                res[10] = x * x2;
                res[11] = x2 * y;
                res[12] = x * y2;
                res[13] = y * y2;
                res[14] = x2 * z;
                res[15] = xy * z;
                res[16] = y2 * z;
                res[17] = x * z2;
                res[18] = y * z2;
                res[19] = z * z2;
                if (diffOrder >= 1) {
                    res_x[0] = 0;
                    res_x[1] = 1;
                    res_x[2] = 0;
                    res_x[3] = 0;
                    res_x[4] = 2 * x;
                    res_x[5] = y;
                    res_x[6] = 0;
                    res_x[7] = z;
                    res_x[8] = 0;
                    res_x[9] = 0;
                    res_x[10] = 3 * x2;
                    res_x[11] = 2 * xy;
                    res_x[12] = y2;
                    res_x[13] = 0;
                    res_x[14] = 2 * xz;
                    res_x[15] = yz;
                    res_x[16] = 0;
                    res_x[17] = z2;
                    res_x[18] = 0;
                    res_x[19] = 0;

                    res_y[0] = 0;
                    res_y[1] = 0;
                    res_y[2] = 1;
                    res_y[3] = 0;
                    res_y[4] = 0;
                    res_y[5] = x;
                    res_y[6] = 2 * y;
                    res_y[7] = 0;
                    res_y[8] = z;
                    res_y[9] = 0;

                    res_y[10] = 0;
                    res_y[11] = x2;
                    res_y[12] = 2 * xy;
                    res_y[13] = 3 * y2;
                    res_y[14] = 0;
                    res_y[15] = xz;
                    res_y[16] = 2 * yz;
                    res_y[17] = 0;
                    res_y[18] = z2;
                    res_y[19] = 0;

                    res_z[0] = 0;
                    res_z[1] = 0;
                    res_z[2] = 0;
                    res_z[3] = 1;
                    res_z[4] = 0;
                    res_z[5] = 0;
                    res_z[6] = 0;
                    res_z[7] = x;
                    res_z[8] = y;
                    res_z[9] = 2 * z;

                    res_z[10] = 0;
                    res_z[11] = 0;
                    res_z[12] = 0;
                    res_z[13] = 0;
                    res_z[14] = x2;
                    res_z[15] = xy;
                    res_z[16] = y2;
                    res_z[17] = 2 * xz;
                    res_z[18] = 2 * yz;
                    res_z[19] = 3 * z2;
                }
                break;
            default:
                throw new IllegalStateException();
        }
        return results;
    }
}
