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

package net.epsilony.tb.solid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import net.epsilony.tb.TestTool;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class FacetTest {

    public FacetTest() {
    }

    @Test
    public void testDistanceFuncSimp() {
        Facet pg = samplePolygon();
        double[][] testCoords = new double[][] { { 0.5, 0.5 }, { 0.1, 0.1 }, { -0.1, -0.1 }, { 0.5, 0.4 },
                { 0.5, 0.25 } };
        double[] exps = new double[] { 0, 0.1, -0.1 * Math.sqrt(2), 0.1, 0.25 };
        for (int i = 0; i < exps.length; i++) {
            double exp = exps[i];
            double act = pg.distanceFunc(testCoords[i][0], testCoords[i][1]);
            assertEquals(exp, act, 1e-12);
        }
    }

    @Test
    public void testDistanceFuncComp() {
        ArrayList<double[][][]> coords = new ArrayList<>(1);
        Facet pg = TestTool.samplePolygon(coords);
        double[][] testCoordsExps = new double[][] { { 3, 6.5, Math.sqrt(2) / 4 }, { 7, 5.5, -Math.sqrt(5) / 10 },
                { -1, -1, -Math.sqrt(2) }, { 1, 2.5, 0 }, { 6.25, 6, 0.25 }, { 2.9, 3, -0.1 } };
        for (double[] xy_exp : testCoordsExps) {
            double exp = xy_exp[2];
            double x = xy_exp[0];
            double y = xy_exp[1];
            double act = pg.distanceFunc(x, y);
            assertEquals(exp, act, 1e-12);
        }
    }

    @Test
    public void testIterable() {
        ArrayList<double[][][]> coords = new ArrayList<>(1);
        Facet pg = TestTool.samplePolygon(coords);
        int i = 0, j = 0;
        for (Segment seg : pg) {
            double[][][] coordChains = coords.get(0);
            double[] coord = coordChains[i][j];
            assertArrayEquals(coord, seg.getStart().coord, 1e-14);
            j++;
            if (j >= coordChains[i].length) {
                i++;
                j = 0;
            }
        }
    }

    @Test
    public void testPolygonSegmentPredLink() {
        Facet pg = TestTool.samplePolygon(null);
        ArrayList<LinkedList<Node>> vertes = pg.getVertes();
        Iterator<LinkedList<Node>> vIter = vertes.iterator();
        for (Segment cHead : pg.getRingsHeads()) {
            Line seg = (Line) cHead;
            LinkedList<Node> cs = vIter.next();
            ListIterator<Node> csIter = cs.listIterator(cs.size());
            boolean getHere = false;
            do {
                Node actNd = seg.pred.getStart();
                Node expNd = csIter.previous();
                assertArrayEquals(expNd.coord, actNd.coord, 1e-14);
                seg = (Line) seg.pred;
                getHere = true;
            } while (seg != cHead);
            assertTrue(getHere);
        }
    }

    @Test
    public void testFractionize() {
        double[][][] coordChains = new double[][][] { { { -1, -1 }, { 1, -1 }, { 1, 1 }, { -1, 1 } },
                { { -0.5, -0.5 }, { -0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, -0.5 } } };

        Facet pg = Facet.byCoordChains(coordChains);
        Facet fPg = pg.fractionize(0.67);
        ArrayList<LinkedList<Node>> pVertes = fPg.getVertes();
        double[][][] exps = new double[][][] {
                { { -1, -1 }, { -0.5, -1 }, { 0, -1 }, { 0.5, -1 }, { 1, -1 }, { 1, -0.5 }, { 1, 0 }, { 1, 0.5 },
                        { 1, 1 }, { 0.5, 1 }, { 0, 1 }, { -0.5, 1 }, { -1, 1 }, { -1, 0.5 }, { -1, 0 }, { -1, -0.5 }, },
                { { -0.5, -0.5 }, { -0.5, 0 }, { -0.5, 0.5 }, { 0, 0.5 }, { 0.5, 0.5 }, { 0.5, 0 }, { 0.5, -0.5 },
                        { 0, -0.5 } } };
        int i = 0;
        for (LinkedList<Node> cs : pVertes) {
            int j = 0;
            for (Node nd : cs) {
                double[] act = nd.coord;
                double[] exp = exps[i][j];
                assertArrayEquals(exp, act, 1e-12);
                j++;
            }
            i++;
        }
    }

    public Facet samplePolygon() {
        double[][][] coordChains = new double[][][] { { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0.5, 0.5 }, { 0, 1 } } };
        return Facet.byCoordChains(coordChains);
    }
}
