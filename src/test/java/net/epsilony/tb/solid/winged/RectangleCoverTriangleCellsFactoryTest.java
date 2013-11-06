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

package net.epsilony.tb.solid.winged;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.epsilony.tb.RudeFactory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RectangleCoverTriangleCellsFactoryTest {

    public RectangleCoverTriangleCellsFactoryTest() {
    }

    @Test
    public void testCoverRectangle() {
        Rectangle2D rect = new Rectangle2D.Double(-1, 1, 15.1, 10.1);
        double edgeLength = 1.5;
        RectangleCoverTriangleCellsFactory factory = new RectangleCoverTriangleCellsFactory();
        factory.setCellFactory(new RudeFactory<>(TriangleCell.class));
        factory.setEdgeFactory(new RudeFactory<>(RawWingedEdge.class));
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

            for (int i = 0; i < 3; i++) {
                WingedEdge edge = cell.getVertexEdge(i);
                assertEquals(edgeLength, Segment2DUtils.chordLength(edge), edgeLength * 0.1);
                double[] startCoord = edge.getStart().getCoord();
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
        checkTriangleLink(cellList);
    }

    public static void checkTriangleLink(List<? extends WingedCell> cellList) {
        Map<WingedCell, Integer> cellMap = new HashMap<>();
        for (WingedCell cell : cellList) {
            for (int i = 0; i < 3; i++) {
                WingedEdge edge = cell.getVertexEdge(i);
                if (null == edge.getOpposite()) {
                    continue;
                }
                assertTrue(edge.getOpposite().getOpposite() == edge);
                assertTrue(edge.getStart() == edge.getOpposite().getEnd());
                assertTrue(edge.getEnd() == edge.getOpposite().getStart());
                TriangleCell oppositeCell = (TriangleCell) edge.getOpposite().getCell();
                assertTrue(oppositeCell != null);
                Integer result = cellMap.get(oppositeCell);
                if (null == result) {
                    result = 0;
                }
                result += 1;
                cellMap.put(oppositeCell, result);
            }
        }

        assertTrue(cellMap.size() == cellList.size());

        for (Map.Entry<WingedCell, Integer> entry : cellMap.entrySet()) {
            Integer value = entry.getValue();
            assertTrue(value <= 3 && value >= 1);
        }
    }
}
