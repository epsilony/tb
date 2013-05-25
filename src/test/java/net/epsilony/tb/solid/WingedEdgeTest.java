/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.tb.solid;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class WingedEdgeTest {

    public WingedEdgeTest() {
    }

    @Test
    public void testBisectWithoutOpposite() {
        WingedEdge edge = new WingedEdge();
        edge.setStart(new Node(1, -1));
        edge.setSucc(new WingedEdge());
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
        List<WingedEdge> chainA = genEdgeChain(coordsA);
        List<WingedEdge> chainB = genEdgeChain(coordsB);

        WingedEdge a = chainA.get(1);
        WingedEdge b = chainB.get(1);

        a.setOpposite(b);
        b.setOpposite(a);

        a.bisect();

        assertTrue(a.getPred() == chainA.get(0));
        assertTrue(a.getSucc().getPred() == a);
        assertTrue(chainA.get(0).getSucc() == a);
        assertTrue(a.getSucc().getOpposite() == b);
        assertTrue(a.getSucc().getSucc() == chainA.get(2));
        assertTrue(a.getSucc().getSucc().getPred() == a.getSucc());

        assertTrue(b.getPred() == chainB.get(0));
        assertTrue(b.getPred().getSucc() == b);
        assertTrue(b.getSucc().getPred() == b);
        assertTrue(b.getSucc().getOpposite() == a);
        assertTrue(b.getSucc().getSucc() == chainB.get(2));
        assertTrue(b.getSucc().getSucc().getPred() == b.getSucc());

        assertArrayEquals(b.getEnd().getCoord(), new double[]{0, 0}, 1e-14);
    }

    public static List<WingedEdge> genEdgeChain(double[][] coords) {

        List<WingedEdge> result = new ArrayList<>(coords.length);
        for (double[] coord : coords) {
            WingedEdge newEdge = new WingedEdge();
            newEdge.setStart(new Node(coord));
            result.add(newEdge);
        }
        for (int i = 0; i < result.size() - 1; i++) {
            Segment2DUtils.link(result.get(i), result.get(i + 1));
        }
        return result;
    }
}