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

import net.epsilony.tb.solid.Facet;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Facet polygon = Facet.byCoordChains(points);

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
