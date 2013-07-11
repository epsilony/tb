/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Node;
import org.junit.Test;
import static org.junit.Assert.*;

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
        QuadrangleAdaptiveCellFactory<Node> factory = new QuadrangleAdaptiveCellFactory<>();
        factory.setNodeClass(Node.class);
        factory.setXs(xs);
        factory.setYs(ys);

        ArrayList<AdaptiveCell<Node>> cellList = new ArrayList<AdaptiveCell<Node>>(factory.produce());

        for (AdaptiveCell<Node> cell : cellList) {
            int count = 0;

            for (AdaptiveCellEdge<Node> edge : cell) {



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

        Map<AdaptiveCell<Node>, Integer> cellMap = new HashMap<>();
        for (AdaptiveCell<Node> cell : cellList) {
            for (AdaptiveCellEdge<Node> edge : cell) {
                if (null == edge.getOpposite()) {
                    continue;
                }
                AdaptiveCell<Node> opposite = edge.getOpposite().getCell();
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