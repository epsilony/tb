/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.solid.SegmentStartCoordIterable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MarchingTriangleTest {

    public MarchingTriangleTest() {
    }

    public class RawOneDiskWithAHole implements DifferentiableFunction {

        int diffOrder = 0;
        double diskX = 50, diskY = 50, diskRad = 40;
        double holeX = 44, holeY = 42, holeRad = 15;

        @Override
        public double[] value(double[] input, double[] output) {
            double diskValue = Math2D.distance(diskX, diskY, input[0], input[1]) - diskRad;
            double holeValue = holeRad - Math2D.distance(holeX, holeY, input[0], input[1]);
            double value = Math.max(diskValue, holeValue);
            if (output == null) {
                return new double[]{value};
            } else {
                output[0] = value;
                return output;
            }
        }

        @Override
        public int getInputDimension() {
            return 2;
        }

        @Override
        public int getOutputDimension() {
            return 1;
        }

        @Override
        public int getDiffOrder() {
            return diffOrder;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            if (diffOrder != 0) {
                throw new IllegalArgumentException("only support 0, not " + diffOrder);
            }
        }
    }

    @Test
    public void testDiskWithAHoleWithoutGradientMethod() {
        TriangleContourCellFactory factory = new TriangleContourCellFactory();
        Rectangle2D range = new Rectangle2D.Double(0, 0, 100, 100);
        double edgeLength = 5;
        int expChainsSize = 2;
        double errRatio = 0.05;
        RawOneDiskWithAHole levelsetFunction = new RawOneDiskWithAHole();
        TriangleContourCell[][] cellsGrid = factory.coverRectangle(range, edgeLength);
        LinkedList<TriangleContourCell> cells = new LinkedList<>();
        MiscellaneousUtils.addToList(cellsGrid, cells);
        TriangleContourBuilder builder = new MarchingTriangle.LinearInterpolate();
        builder.setCells(cells);
        builder.setLevelSetFunction(levelsetFunction);
        builder.genContour();
        List<Line2D> contourHeads = builder.getContourHeads();

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