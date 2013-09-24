/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.List;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.WingedEdge;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveCellEdgeTest {

    public AdaptiveCellEdgeTest() {
    }

    @Test
    public void testBisectWithoutOpposite() {
        AdaptiveCellEdge edge = new AdaptiveCellEdge();
        edge.setStart(new Node(1, -1));
        edge.setSucc(new AdaptiveCellEdge());
        edge.getSucc().setStart(new Node(3, 4));
        edge.bisect();
        assertArrayEquals(edge.getEnd().getCoord(), new double[]{2, 1.5}, 1e-13);
        assertTrue(edge.getSucc().getPred() == edge);
        assertArrayEquals(edge.getSucc().getEnd().getCoord(), new double[]{3, 4}, 1e-14);
        assertTrue(edge.getSucc().getSucc().getPred() == edge.getSucc());
    }

    @Test
    public void testBisect() {
        double[][] coordsA = new double[][]{{-1, 1}, {0, 1}, {0, -1}, {-1, -1}};
        double[][] coordsB = new double[][]{{1, -1}, {0, -1}, {0, 1}, {1, 1}};
        List<AdaptiveCellEdge> chainA = genEdgeChain(coordsA);
        List<AdaptiveCellEdge> chainB = genEdgeChain(coordsB);

        AdaptiveCellEdge a = chainA.get(1);
        AdaptiveCellEdge b = chainB.get(1);
        final TriangleAdaptiveCell cellA = new TriangleAdaptiveCell();
        a.setCell(cellA);
        final QuadrangleAdaptiveCell cellB = new QuadrangleAdaptiveCell();
        b.setCell(cellB);

        a.setOpposite(b);
        b.setOpposite(a);

        a.bisect();

        assertTrue(a.getPred() == chainA.get(0));
        assertTrue(a.getSucc().getPred() == a);
        assertTrue(chainA.get(0).getSucc() == a);
        assertTrue(((WingedEdge) a.getSucc()).getOpposite() == b);
        assertTrue(a.getSucc() == b.getOpposite());
        assertTrue(a.getSucc().getSucc() == chainA.get(2));
        assertTrue(a.getSucc().getSucc().getPred() == a.getSucc());
        assertTrue(a.getCell() == cellA);
        assertTrue(((WingedEdge) a.getSucc()).getCell() == cellA);

        assertTrue(b.getPred() == chainB.get(0));
        assertTrue(b.getPred().getSucc() == b);
        assertTrue(b.getSucc().getPred() == b);
        assertTrue(((WingedEdge) b.getSucc()).getOpposite() == a);
        assertTrue(b.getSucc() == a.getOpposite());
        assertTrue(b.getSucc().getSucc() == chainB.get(2));
        assertTrue(b.getSucc().getSucc().getPred() == b.getSucc());
        assertTrue(b.getCell() == cellB);
        assertTrue(((WingedEdge) b.getSucc()).getCell() == cellB);

        assertArrayEquals(b.getEnd().getCoord(), new double[]{0, 0}, 1e-14);
    }

    public static List<AdaptiveCellEdge> genEdgeChain(double[][] coords) {

        List<AdaptiveCellEdge> result = new ArrayList<>(coords.length);
        for (double[] coord : coords) {
            AdaptiveCellEdge newEdge = new AdaptiveCellEdge();
            newEdge.setStart(new Node(coord));
            result.add(newEdge);
        }
        for (int i = 0; i < result.size() - 1; i++) {
            Segment2DUtils.link(result.get(i), result.get(i + 1));
        }
        return result;
    }
}
