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
