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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Math2DTest {

    public Math2DTest() {
    }

    /**
     * Test of isSegmentsIntersecting method, of class Math2D.
     */
    @Test
    public void testIsSegmentsIntersecting() {
        double[][][] samples = new double[][][] { { { 1, 1 }, { 5, 2 }, { 2, 1 }, { 2, 5 } },
                { { 1, 1 }, { 5, 2 }, { -0.5, 1 }, { 2, 5 } }, { { 1, 1 }, { 5, 2 }, { 6.1, 1 }, { 2, 5 } },
                { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } }, { { 0, 0 }, { 1, 0 }, { 0.5, 0 }, { 3, 0 } },
                { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } }, { { 0, 0 }, { 0, 1 }, { 0, 0.5 }, { 0, 3 } }, };
        boolean[] exps = new boolean[] { true, false, false, false, true, false, true };
        for (int i = 0; i < exps.length; i++) {
            boolean act = Math2D.isSegmentsIntersecting(samples[i][0], samples[i][1], samples[i][2], samples[i][3]);
            assertEquals(exps[i], act);
        }
    }

    @Test
    public void testIntersectionPointsOfTwoSegments() {
        double[][] inputs = new double[][] { { -2, 2 }, { 3, 3 }, { 2, 5 }, { 3, -2 } };
        double[] exp = new double[] { 83 / 36d, 103 / 36d };
        double[] act = Math2D.intersectionPoint(inputs[0], inputs[1], inputs[2], inputs[3], null);
        assertArrayEquals(exp, act, 1e-13);
    }

    @Test
    public void testNormailize() {
        double[] sample = new double[] { -3, 4 };
        double[] exp = new double[] { -3 / 5d, 4 / 5d };
        double[] act = Math2D.normalize(sample, null);
        double[] act2 = Math2D.normalize(sample, sample);
        assertArrayEquals(exp, act, 1e-15);
        assertArrayEquals(exp, act2, 1e-15);
    }

    @Test
    public void testCos() {
        double[] sample = new double[] { Math.sqrt(3), 1, 0, 15 };
        double exp = 0.5;
        double act = Math2D.cos(sample[0], sample[1], sample[2], sample[3]);
        assertEquals(exp, act, 1e-15);
    }

    @Test
    public void testIsClockWise() {
        double[][] points = new double[][] { { 0, 0 }, { 1, 0 }, { 0.4, 0.4 }, { 0.8, 1 } };
        boolean exp = true;
        boolean act = Math2D.isAnticlockwise(Arrays.asList(points));

        assertEquals(exp, act);
    }

    @Test
    public void testArea() {
        double[][][] vertess = new double[][][] { { { -1, -2 }, { 3, 2 }, { 2, 4 } },
                { { -1, -2 }, { 2, 4 }, { 3, 2 } }, { { 0, 0 }, { 2, 0 }, { 2, 2 }, { 1, 1 }, { 0, 2 } } };
        double[] expAreas = { 6, -6, 3 };
        for (int i = 0; i < expAreas.length; i++) {
            double[][] vertes = vertess[i];
            double expArea = expAreas[i];
            double actArea = Math2D.area(vertes);
            double actArea2 = Math2D.area(Arrays.asList(vertes));
            assertEquals(expArea, actArea, 1e-14);
            assertEquals(expArea, actArea2, 1e-14);
        }
    }

    @Test
    public void testCentroid() {
        double[][] vertes = new double[][] { { -1, -2 }, { 3, 2 }, { 2, 4 } };
        double[] expCentroid = new double[] { 4 / 3.0, 4 / 3.0 };
        double[] actCentroid = Math2D.centroid(vertes, null);
        assertArrayEquals(expCentroid, actCentroid, 1e-14);
    }

    @Test
    public void testProjectionParameter() {
        double[] start = new double[] { -1, -2 };
        double[] end = new double[] { 3, 2 };
        double[][] points = new double[][] { { -1, -2 }, { 3, 2 }, { 1, 0 }, { 2, 0 } };
        double[] exps = new double[] { 0, 1, 0.5, 0.625 };

        for (int i = 0; i < exps.length; i++) {
            double exp = exps[i];
            double[] pt = points[i];
            double act = Math2D.projectionParameter(start, end, pt);
            assertEquals(exp, act, 1e-14);
        }
    }
}
