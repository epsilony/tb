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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Node;

import org.junit.Test;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCellFactoryTest {

    public QuadrangleAdaptiveCellFactoryTest() {
    }

    /**
     * Test of byCoordGrid method, of class QuadrangleAdaptiveCellFactory.
     */
    @Test
    public void testByCoordGrid() {
        double[] xs = TestTool.linSpace(0, 200, 10);
        double[] ys = TestTool.linSpace(100, 0, 5);
        QuadrangleAdaptiveCellFactory factory = new QuadrangleAdaptiveCellFactory();
        factory.setNodeClass(Node.class);
        factory.setXs(xs);
        factory.setYs(ys);

        ArrayList<AdaptiveCell> cellList = new ArrayList<AdaptiveCell>(factory.produce());

        for (AdaptiveCell cell : cellList) {
            int count = 0;

            for (AdaptiveCellEdge edge : cell) {

                if (edge.getOpposite() != null) {
                    assertEquals(edge.getOpposite().getOpposite(), edge);
                }
                // double[] startCoord = edge.getStartCoord();
                // double x = startCoord[0];
                // double y = startCoord[1];

                count++;
            }
            assertEquals(4, count);
        }

        Map<AdaptiveCell, Integer> cellMap = new HashMap<>();
        for (AdaptiveCell cell : cellList) {
            for (AdaptiveCellEdge edge : cell) {
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

        int numCol = xs.length - 1;
        int numRow = ys.length - 1;

        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                int exp;
                if (i == 0 || i == numRow - 1) {
                    if (j == 0 || j == numCol - 1) {
                        exp = 2;
                    } else {
                        exp = 3;
                    }
                } else if (j == 0 || j == numCol - 1) {
                    exp = 3;
                } else {
                    exp = 4;
                }
                assertEquals(exp, (int) cellMap.get(cellList.get(i * numCol + j)));
            }
        }
    }
}
