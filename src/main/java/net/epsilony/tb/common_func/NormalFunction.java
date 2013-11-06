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

import static java.lang.Math.*;
import net.epsilony.tb.MiscellaneousUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NormalFunction implements RadialBasisCore {
    // 1/(sigma*sqrt(PI*2))*exp(-x*x/(2*sigma*sigma))

    public static double DEFAULT_SIGMA = 1;
    int diffOrder;
    double sigma = DEFAULT_SIGMA;
    double coef = 1 / (sigma * sqrt(PI * 2));

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        coef = 1 / (sigma * sqrt(PI * 2));
    }

    @Override
    public double[] valuesByDistance(double distance, double[] results) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance should be >= 0, not" + distance);
        }
        if (null == results) {
            results = new double[]{diffOrder + 1};
        }
        double t = distance / sigma;
        final double v = coef * exp(-0.5 * t * t);
        results[0] = v;
        if (diffOrder >= 1) {
            results[1] = -t * v / sigma;
        }
        return results;
    }

    @Override
    public double[] valuesByDistanceSquare(double distanceSquare, double[] results) {
        if (null == results) {
            results = new double[diffOrder + 1];
        }
        double sigmaSquare = (sigma * sigma);
        double u = distanceSquare / sigmaSquare;
        double v = coef * exp(-0.5 * u);
        results[0] = v;
        for (int i = 0; i < diffOrder; i++) {
            v = v * (-0.5 / sigmaSquare);
            results[i + 1] = v;
        }
        return results;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("only supports 0 or 1");
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public String toString() {
        return MiscellaneousUtils.simpleToString(this) + '{' + "sigma=" + sigma + '}';
    }
}
