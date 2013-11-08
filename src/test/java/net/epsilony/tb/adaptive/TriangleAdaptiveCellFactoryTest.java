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

package net.epsilony.tb.adaptive;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.WingedCell;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCellFactoryTest {

    public TriangleAdaptiveCellFactoryTest() {
    }

    /**
     * Test of coverRectangle method, of class TriangleAdaptiveCellFactory.
     */
    @Test
    public void testCoverRectangle() {
        Rectangle2D rect = new Rectangle2D.Double(-1, 1, 15.1, 10.1);
        double edgeLength = 1.5;
        TriangleAdaptiveCellFactory factory = new TriangleAdaptiveCellFactory();
        factory.setNodeFactory(Node.factory());
        factory.setRectangle(rect);
        factory.setEdgeLength(edgeLength);
        List<WingedCell> cellList = factory.produce();
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (WingedCell cell : cellList) {
            int count = 0;

            for (AdaptiveCellEdge edge : (AdaptiveCell) cell) {

                assertEquals(edgeLength, Segment2DUtils.chordLength(edge), edgeLength * 0.1);

                if (edge.getOpposite() != null) {
                    assertEquals(edge.getOpposite().getOpposite(), edge);
                }
                double[] startCoord = edge.getStartCoord();
                double x = startCoord[0];
                double y = startCoord[1];
                minX = Math.min(x, minX);
                minY = Math.min(y, minY);
                maxX = Math.max(x, maxX);
                maxY = Math.max(y, maxY);

                count++;
            }
            assertEquals(3, count);
        }

        assertTrue(minX < rect.getMinX());
        assertTrue(minY < rect.getMinY());
        assertTrue(maxX > rect.getMaxX());
        assertTrue(maxY > rect.getMaxY());

        Map<AdaptiveCell, Integer> cellMap = new HashMap<>();
        for (WingedCell cell : cellList) {
            for (AdaptiveCellEdge edge : (AdaptiveCell) cell) {
                if (null == edge.getOpposite()) {
                    continue;
                }
                AdaptiveCell opposite = (AdaptiveCell) edge.getOpposite().getCell();
                assertTrue(opposite != null);
                Integer result = cellMap.get(opposite);
                if (null == result) {
                    result = 0;
                }
                result += 1;
                cellMap.put(opposite, result);
            }
        }

        for (Map.Entry<AdaptiveCell, Integer> entry : cellMap.entrySet()) {
            Integer value = entry.getValue();
            assertTrue(value <= 3 && value >= 1);
        }
    }
}
