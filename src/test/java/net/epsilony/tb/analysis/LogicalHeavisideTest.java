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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class LogicalHeavisideTest {

    public LogicalHeavisideTest() {
    }

    @Test
    public void testValues() {
        LogicalHeaviside lh = new LogicalHeaviside();
        lh.setDiffOrder(1);

        final double k = 11;
        lh.setK(k);
        double[][] xsExps = new double[][] { { 0, 0.5, k / 2 }, { -0.3, 0.0013585199504289591, 0.029846836227411404 },
                { 0.4, 0.9998492896419403, 0.0033151281778500807 }, { 100, 1, 0 }, { -100, 0, 0 } };

        for (double[] xExp : xsExps) {
            double x = xExp[0];
            double exp = xExp[1];
            double dexp = xExp[2];
            double[] values = lh.values(x, null);
            double value = lh.value(x);
            assertEquals(exp, values[0], 1e-14);
            assertEquals(exp, value, 1e-14);
            assertEquals(dexp, values[1], 1e-14 * dexp);
        }
    }

    @Test
    public void testErrKRelated() {
        LogicalHeaviside lh = new LogicalHeaviside();
        lh.setK(1, 0.15);
        assertEquals(0.15, lh.getErr(1), 1e-14);
        assertEquals(0.15, lh.getErr(-1), 1e-14);
        lh.setK(1, 0.15, true);
        assertTrue(0.15 >= lh.getErr(1));
        assertTrue(0.15 >= lh.getErr(-1));
    }
}
