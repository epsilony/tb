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

package net.epsilony.tb.implicit;

import java.awt.geom.Rectangle2D;
import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.LogicalMaximum;
import net.epsilony.tb.solid.SegmentStartCoordIterable;
import net.epsilony.tb.solid.winged.WingedCell;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TrackContourBuilderTest {

    public TrackContourBuilderTest() {
    }

    double diskCenterX = 10;
    double diskCenterY = -20;
    double diskRadius = 50;
    double holeCenterX = 15;
    double holeCenterY = -15;
    double holeRadius = 20;

    public class SampleOneDiskWithAHole implements DifferentiableFunction {

        LogicalMaximum max = new LogicalMaximum();
        private CircleLevelSet disk;
        private CircleLevelSet hole;

        public SampleOneDiskWithAHole() {
            max.setK(diskRadius - holeRadius, 1e-10, true);
            disk = new CircleLevelSet(diskCenterX, diskCenterY, diskRadius);
            disk.setConcrete(true);
            hole = new CircleLevelSet(holeCenterX, holeCenterY, holeRadius);
            hole.setConcrete(false);
            max.setFunctions(disk, hole);
        }

        @Override
        public double[] value(double[] input, double[] output) {
            return max.value(input, output);
        }

        @Override
        public int getDiffOrder() {
            return max.getDiffOrder();
        }

        @Override
        public int getInputDimension() {
            return max.getInputDimension();
        }

        @Override
        public int getOutputDimension() {
            return max.getOutputDimension();
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            max.setDiffOrder(diffOrder);
        }
    }

    @Test
    public void testDiskWithAHole() {
        TriangleContourCellFactory factory = new TriangleContourCellFactory();
        double edgeLength = 5;
        int expChainsSize = 2;
        double errRatio = 0.05;

        Rectangle2D range = new Rectangle2D.Double(diskCenterX - diskRadius - edgeLength * 2, diskCenterY - diskRadius
                - edgeLength * 2, diskRadius * 2 + edgeLength * 4, diskRadius * 2 + edgeLength * 4);

        SampleOneDiskWithAHole levelsetFunction = new SampleOneDiskWithAHole();
        factory.setRectangle(range);
        factory.setEdgeLength(edgeLength);
        List<WingedCell> cells = factory.produce();
        TrackContourBuilder builder = new TrackContourBuilder();
        builder.setCells((List) cells);
        builder.setLevelSetFunction(levelsetFunction);
        SimpleGradientSolver solver = new SimpleGradientSolver();
        solver.setMaxEval(200);
        builder.setImplicitFunctionSolver(solver);
        builder.genContour();
        List<Line> contourHeads = builder.getContourHeads();

        assertEquals(expChainsSize, contourHeads.size());

        for (int i = 0; i < contourHeads.size(); i++) {
            double x0, y0, rad;
            Line head = contourHeads.get(i);
            boolean b = Math2D.isAnticlockwise(new SegmentStartCoordIterable(head));
            if (b) {
                x0 = diskCenterX;
                y0 = diskCenterY;
                rad = diskRadius;
            } else {
                x0 = holeCenterX;
                y0 = holeCenterY;
                rad = holeRadius;
            }
            double expArea = Math.PI * rad * rad;
            expArea *= b ? 1 : -1;

            Line seg = head;
            double actArea = 0;
            double[] center = new double[] { x0, y0 };
            do {
                double[] startCoord = seg.getStart().getCoord();
                double[] endCoord = seg.getEnd().getCoord();
                actArea += 0.5 * Math2D.cross(endCoord[0] - startCoord[0], endCoord[1] - startCoord[1], x0
                        - startCoord[0], y0 - startCoord[1]);
                seg = (Line) seg.getSucc();
                double actRadius = Math2D.distance(startCoord, center);
                assertEquals(rad, actRadius, 1e-5);
                actRadius = Math2D.distance(endCoord, center);
                assertEquals(rad, actRadius, 1e-5);
            } while (seg != head);
            assertEquals(expArea, actArea, Math.abs(expArea) * errRatio);
        }
    }
}
