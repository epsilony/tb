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

package net.epsilony.tb.quadrature;

import net.epsilony.tb.analysis.ArrvarFunction;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleQuadratureTest {

    public QuadrangleQuadratureTest() {
    }

    @Test
    public void testArea() {
        double[] quad = new double[]{4, 4, -2.1, 1.6, 0.8, -1.9, 3, 1.1};
        QuadrangleQuadrature qQuad = new QuadrangleQuadrature();
        qQuad.setQuadrangle(quad);
        final double val = -1.4;
        double exp = 15.845 * val;
        for (int degree = GaussLegendre.MINPOINTS * 2 - 1; degree <= GaussLegendre.MAXPOINTS * 2 - 1; degree++) {
            qQuad.setDegree(degree);
            double act = qQuad.quadrate(new ArrvarFunction() {
                @Override
                public double value(double[] vec) {
                    return val;
                }
            });
            assertEquals(exp, act, 1e-12);
        }
    }

    @Test
    public void testX() {
        double[] quad = new double[]{0, 0, 4, 0, 4, 3, 0, 3};
        QuadrangleQuadrature qQuad = new QuadrangleQuadrature();
        qQuad.setQuadrangle(quad);
        double exp = 24;
        for (int degree = GaussLegendre.MINPOINTS * 2 - 1; degree <= GaussLegendre.MAXPOINTS * 2 - 1; degree++) {
            qQuad.setDegree(degree);
            double act = qQuad.quadrate(new ArrvarFunction() {
                @Override
                public double value(double[] vec) {
                    return vec[0];
                }
            });
            assertEquals(exp, act, 1e-12);
        }
    }

    @Test
    public void testY() {
        double[] quad = new double[]{0, 0, 4, 0, 4, 3, 0, 3};
        QuadrangleQuadrature qQuad = new QuadrangleQuadrature();
        qQuad.setQuadrangle(quad);
        double exp = 18;
        for (int degree = GaussLegendre.MINPOINTS * 2 - 1; degree <= GaussLegendre.MAXPOINTS * 2 - 1; degree++) {
            qQuad.setDegree(degree);
            double act = qQuad.quadrate(new ArrvarFunction() {
                @Override
                public double value(double[] vec) {
                    return vec[1];
                }
            });
            assertEquals(exp, act, 1e-12);
        }
    }
}
