/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.analysis.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MarchingTriangleContourBuilder extends AbstractTriangleContourBuilder {
    protected LinkedList<TriangleContourCell> openRingHeadCells;
    protected LinkedList<Line2D> openRingHeadSegments;
    protected Iterator<TriangleContourCell> cellsIterator;

    @Override
    public void genContour() {
        prepareGenContour();
        while (true) {
            TriangleContourCell headCell = nextUnvisitedCellWithContour();
            if (null == headCell) {
                break;
            }
            genContourFromCell(headCell);
        }
    }

    private void prepareGenContour() {
        prepareCellAndNodes();

        contourHeads = new LinkedList<>();
        openRingHeadCells = new LinkedList<>();
        openRingHeadSegments = new LinkedList<>();
        cellsIterator = cells.iterator();
    }

    private TriangleContourCell nextUnvisitedCellWithContour() {
        TriangleContourCell result = null;
        while (cellsIterator.hasNext()) {
            TriangleContourCell cell = cellsIterator.next();
            if (cell.isVisited()) {
                continue;
            }
            setupFunctionData(cell);

            Line2D sourceEdge = cell.getContourSourceEdge();
            if (sourceEdge == null) {
                cell.setVisited(true);
                continue;
            }
            result = cell;
            break;
        }
        return result;
    }

    private void genContourFromCell(TriangleContourCell headCell) {
        headCell.setVisited(true);
        Line2D chainHead = new Line2D(genContourNode(headCell.getContourSourceEdge()));
        contourHeads.add(chainHead);

        openRingHeadCells.add(headCell);
        openRingHeadSegments.add(chainHead);
        TriangleContourCell contourCell = headCell;

        Line2D segment = chainHead;
        while (true) {
            TriangleContourCell nextContourCell = contourCell.nextContourCell();
            if (null == nextContourCell) {
                Line2D newSucc = new Line2D(genContourNode(contourCell.getContourDestinationEdge()));
                Segment2DUtils.link(segment, newSucc);
                break;
            } else {
                contourCell = nextContourCell;
            }

            if (contourCell == headCell) {
                Segment2DUtils.link(segment, chainHead);
                openRingHeadCells.remove(headCell);
                openRingHeadSegments.remove(chainHead);
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

            Line2D newSucc = new Line2D(genContourNode(contourCell.getContourSourceEdge()));
            Segment2DUtils.link(segment, newSucc);
            segment = newSucc;

        }
    }

    private Node genContourNode(Line2D contourSourceEdge) {
        if (null != newtonSolver) {
            return genContourNodeByNewtonMethod(contourSourceEdge);
        } else {
            return genContourNodeByLinearInterpolate(contourSourceEdge);
        }
    }

    private Node genContourNodeByLinearInterpolate(Line2D contourSourceEdge) {
        double[] resultCoord = genContourNodeCoordByLinearInterpolate(contourSourceEdge);
        return new Node(resultCoord);
    }

    private double[] genContourNodeCoordByLinearInterpolate(Line2D contourSourceEdge) {
        double[] startCoord = contourSourceEdge.getStart().getCoord();
        double[] endCoord = contourSourceEdge.getEnd().getCoord();
        double startValue = nodesValuesMap.get(contourSourceEdge.getStart())[0];
        double endValue = nodesValuesMap.get(contourSourceEdge.getEnd())[0];
        double t = startValue / (startValue - endValue);
        double[] resultCoord = Math2D.pointOnSegment(startCoord, endCoord, t, null);
        return resultCoord;
    }

    private Node genContourNodeByNewtonMethod(Line2D contourSourceEdge) {
        double[] startPoint = genContourNodeCoordByLinearInterpolate(contourSourceEdge);
        if (newtonSolver.solve(startPoint)) {
            return new Node(newtonSolver.getSolution());
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean tryMergeWithOpenRingHeads(TriangleContourCell contourCell, Line2D segment) {
        Iterator<TriangleContourCell> openHeadCellIter = openRingHeadCells.descendingIterator();
        Iterator<Line2D> openHeadSegIter = openRingHeadSegments.descendingIterator();
        boolean findAndRemove = false;
        while (openHeadCellIter.hasNext()) {
            TriangleContourCell cell = openHeadCellIter.next();
            Line2D openRingHead = openHeadSegIter.next();
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
}