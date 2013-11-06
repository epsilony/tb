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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.WingedEdge;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class MarchingTriangle extends AbstractTriangleContourBuilder {

    protected List<TriangleContourCell> openRingsHeadsCells = new LinkedList<>();

    @Override
    public void genContour() {
        prepareToGenContour();
        while (true) {
            TriangleContourCell headCell = nextUnvisitedCellWithContour();
            if (null == headCell) {
                break;
            }
            genContourFromCell(headCell);
        }
    }

    @Override
    public void prepareToGenContour() {
        super.prepareToGenContour();
        openRingsHeadsCells.clear();
    }

    private void genContourFromCell(TriangleContourCell headCell) {
        headCell.setVisited(true);
        Line chainHead = new Line(genContourNode(headCell.getContourSourceEdge()));
        contourHeads.add(chainHead);

        openRingsHeadsCells.add(headCell);
        openRingsHeads.add(chainHead);
        TriangleContourCell contourCell = headCell;

        Line segment = chainHead;
        while (true) {
            TriangleContourCell nextContourCell = contourCell.nextContourCell();
            if (null == nextContourCell) {
                Line newSucc = new Line(genContourNode(contourCell.getContourDestinationEdge()));
                Segment2DUtils.link(segment, newSucc);
                break;
            } else {
                contourCell = nextContourCell;
            }

            if (contourCell == headCell) {
                Segment2DUtils.link(segment, chainHead);
                openRingsHeadsCells.remove(headCell);
                openRingsHeads.remove(chainHead);
                break;
            }

            if (contourCell.isVisited()) {
                boolean merged = tryMergeWithOpenRingHeads(contourCell, segment);
                if (merged) {
                    break;
                }
                throw new IllegalStateException();
            }

            contourCell.setVisited(true);
            setupFunctionData(contourCell);

            Line newSucc = new Line(genContourNode(contourCell.getContourSourceEdge()));
            Segment2DUtils.link(segment, newSucc);
            segment = newSucc;

        }
    }

    abstract protected ContourNode genContourNode(WingedEdge contourSourceEdge);

    private boolean tryMergeWithOpenRingHeads(TriangleContourCell contourCell, Line segment) {
        Iterator<TriangleContourCell> openHeadCellIter = openRingsHeadsCells.iterator();
        Iterator<Line> openHeadSegIter = openRingsHeads.iterator();
        boolean findAndRemove = false;
        while (openHeadCellIter.hasNext()) {
            TriangleContourCell cell = openHeadCellIter.next();
            Line openRingHead = openHeadSegIter.next();
            if (cell == contourCell) {
                openHeadCellIter.remove();
                openHeadSegIter.remove();
                Segment2DUtils.link(segment, openRingHead);

                contourHeads.remove(openRingHead);

                findAndRemove = true;
                break;
            }
        }
        return findAndRemove;
    }

    public static class LinearInterpolate extends MarchingTriangle {

        @Override
        protected ContourNode genContourNode(WingedEdge contourSourceEdge) {
            double[] resultCoord = genLinearInterpolateContourPoint(contourSourceEdge);
            ContourNode result = new ContourNode();
            result.setCoord(resultCoord);
            return result;
        }
    }

    public static class FreeGradient extends MarchingTriangle {

        ImplicitFunctionSolver solver = new SimpleGradientSolver();

        @Override
        protected ContourNode genContourNode(WingedEdge contourSourceEdge) {
            double[] startPoint = genLinearInterpolateContourPoint(contourSourceEdge);
            if (solver.solve(startPoint)) {
                ContourNode result = new ContourNode();
                result.setCoord(solver.getSolution());
                result.setFunctionValue(solver.getFunctionValue());
                return result;
            } else {
                throw new IllegalStateException();
            }
        }

        public ImplicitFunctionSolver getSolver() {
            return solver;
        }

        public void setSolver(ImplicitFunctionSolver implicitFunctionSolver) {
            this.solver = implicitFunctionSolver;
            implicitFunctionSolver.setFunction(levelSetFunction);
        }
    }

    public static class OnEdge extends MarchingTriangle {

        BoundedImplicitFunctionSolver solver = new SimpleBisectionSolver();
        OnLineFunction onEdgeFunction = new OnLineFunction();

        public OnEdge() {
            solver.setFunction(onEdgeFunction);
            solver.setLowerBounds(new double[]{0});
            solver.setUpperBounds(new double[]{1});
            solver.setMaxEval(200);
            solver.setFunctionAbsoluteTolerence(1e-5);
        }

        public BoundedImplicitFunctionSolver getSolver() {
            return solver;
        }

        public void setSolver(BoundedImplicitFunctionSolver solver) {
            this.solver = solver;
            solver.setLowerBounds(new double[]{0});
            solver.setUpperBounds(new double[]{1});
            solver.setFunction(onEdgeFunction);
        }
        private final double[] solveStart = new double[]{0.5};

        @Override
        protected ContourNode genContourNode(WingedEdge contourSourceEdge) {
            onEdgeFunction.prepareToSolve(contourSourceEdge.getStart().getCoord(), contourSourceEdge.getEnd().getCoord());
            solveStart[0] = genLinearInterpolateParameter(contourSourceEdge);
            if (!solver.solve(solveStart)) {
                solver.solve(solveStart);
                throw new IllegalStateException();
            }
            ContourNode result = new ContourNode();
            result.setCoord(onEdgeFunction.coord);
            result.setFunctionValue(onEdgeFunction.levelSetFuncVal);
            return result;
        }
    }
}
