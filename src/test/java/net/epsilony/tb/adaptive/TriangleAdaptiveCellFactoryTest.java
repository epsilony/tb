/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.tb.adaptive;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.solid.Segment2DUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
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
        TriangleAdaptiveCell[][] cellGrid = factory.coverRectangle(rect, edgeLength);
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        LinkedList<TriangleAdaptiveCell> cellList = new LinkedList<>();
        MiscellaneousUtils.addToList(cellGrid, cellList);
        for (TriangleAdaptiveCell cell : cellList) {
            int count = 0;

            for (AdaptiveCellEdge edge : cell) {

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
        for (TriangleAdaptiveCell cell : cellList) {
            for (AdaptiveCellEdge edge : cell) {
                if (null == edge.getOpposite()) {
                    continue;
                }
                AdaptiveCell opposite = edge.getOpposite().getCell();
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