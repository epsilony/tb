/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.tb.adaptive;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.TestTool;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class QuadrangleAdaptiveCellFactoryTest {

    public QuadrangleAdaptiveCellFactoryTest() {
    }

    /**
     * Test of byCoordGrid method, of class QuadrangleAdaptiveCellFactory.
     */
    @Test
    public void testByCoordGrid() {
        AdaptiveCell[][] cellGrid = QuadrangleAdaptiveCellFactory.byCoordGrid(
                TestTool.linSpace(0, 200, 10), TestTool.linSpace(100, 0, 5));
        LinkedList<AdaptiveCell> cellList = new LinkedList<>();
        MiscellaneousUtils.addToList(cellGrid, cellList);
        for (AdaptiveCell cell : cellList) {
            int count = 0;

            for (AdaptiveCellEdge edge : cell) {



                if (edge.getOpposite() != null) {
                    assertEquals(edge.getOpposite().getOpposite(), edge);
                }
                double[] startCoord = edge.getStartCoord();
                double x = startCoord[0];
                double y = startCoord[1];

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


        for (int i = 0; i < cellGrid.length; i++) {
            for (int j = 0; j < cellGrid[0].length; j++) {
                int exp;
                if (i == 0 || i == cellGrid.length - 1) {
                    if (j == 0 || j == cellGrid[0].length - 1) {
                        exp = 2;
                    } else {
                        exp = 3;
                    }
                } else if (j == 0 || j == cellGrid[0].length - 1) {
                    exp = 3;
                } else {
                    exp = 4;
                }
                assertEquals(exp, (int) cellMap.get(cellGrid[i][j]));
            }
        }
    }
}