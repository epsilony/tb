/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import net.epsilony.tb.solid.Polygon2D;
import org.junit.Test;
import static org.junit.Assert.*;

/* (c) Copyright by Man YUAN */
public class PolygonQuadratureTest {

    public PolygonQuadratureTest() {
    }

    @Test
    public void testIterable() {
        double[][][] points = new double[][][]{
            {
                {-1, -1}, {2, 0}, {0, 2}
            }
        };

        double expLen = Math.sqrt(10) * 2 + Math.sqrt(8);
        Polygon2D polygon = Polygon2D.byCoordChains(points);

        PolygonQuadrature qp = new PolygonQuadrature();
        qp.setPolygon(polygon);
        qp.setDegree(2);

        double len = 0;
        for (Segment2DQuadraturePoint pt : qp) {
            len += pt.weight;
        }

        assertEquals(expLen, len, 1e-12);

    }
}