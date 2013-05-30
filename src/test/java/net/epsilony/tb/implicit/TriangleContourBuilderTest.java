/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.solid.SegmentStartCoordIterable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourBuilderTest {

    public TriangleContourBuilderTest() {
    }

    @Test
    public void testDiskWithAHole() {
        TriangleContourCellFactory factory = new TriangleContourCellFactory();
        Rectangle2D range = new Rectangle2D.Double(0, 0, 100, 100);
        double edgeLength = 5;
        int expChainsSize = 2;
        double errRatio = 0.05;
        DiskWithAHoleLevelSetFunction levelsetFunction = new DiskWithAHoleLevelSetFunction();
        TriangleContourCell[][] cellsGrid = factory.coverRectangle(range, edgeLength);
        LinkedList<TriangleContourCell> cells = new LinkedList<>();
        MiscellaneousUtils.addToList(cellsGrid, cells);
        TriangleContourBuilder builder = new TriangleContourBuilder();
        builder.cells = cells;
        builder.levelSetFunction = levelsetFunction;
        builder.genContour();
        List<Line2D> contourHeads = builder.contourHeads;

        assertEquals(expChainsSize, contourHeads.size());

        for (int i = 0; i < contourHeads.size(); i++) {
            double x0, y0, rad;
            Line2D head = contourHeads.get(i);
            boolean b = Math2D.isAnticlockwise(new SegmentStartCoordIterable(head));
            if (b) {
                x0 = levelsetFunction.diskX;
                y0 = levelsetFunction.diskY;
                rad = levelsetFunction.diskRad;
            } else {
                x0 = levelsetFunction.holeX;
                y0 = levelsetFunction.holeY;
                rad = levelsetFunction.holeRad;
            }
            double expArea = Math.PI * rad * rad;
            expArea *= b ? 1 : -1;

            Line2D seg = head;
            double actArea = 0;
            do {
                double[] startCoord = seg.getStart().getCoord();
                double[] endCoord = seg.getEnd().getCoord();
                actArea += 0.5 * Math2D.cross(endCoord[0] - startCoord[0], endCoord[1] - startCoord[1],
                        x0 - startCoord[0], y0 - startCoord[1]);
                seg = (Line2D) seg.getSucc();
            } while (seg != head);
            assertEquals(expArea, actArea, Math.abs(expArea) * errRatio);
        }

    }
}