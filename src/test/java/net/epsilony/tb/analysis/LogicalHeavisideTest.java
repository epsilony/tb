/* (c) Copyright by Man YUAN */
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
        double[][] xsExps = new double[][]{
            {0, 0.5, k / 2},
            {-0.3, 0.0013585199504289591, 0.029846836227411404},
            {0.4, 0.9998492896419403, 0.0033151281778500807},
            {100,1,0},
            {-100,0,0}
        };

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