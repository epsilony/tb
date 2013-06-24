/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.adaptive.AdaptiveCellEdge;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractTriangleContourBuilder implements TriangleContourBuilder {

    protected List<TriangleContourCell> cells;
    protected DifferentiableFunction levelSetFunction;
    protected List<Line2D> contourHeads = new LinkedList<>();
    protected Iterator<TriangleContourCell> cellsIterator;
    protected List<Line2D> openRingsHeads = new LinkedList<>();

    @Override
    public List<TriangleContourCell> getCells() {
        return cells;
    }

    @Override
    public DifferentiableFunction getLevelSetFunction() {
        return levelSetFunction;
    }

    @Override
    public void setCells(List<TriangleContourCell> cells) {
        this.cells = cells;
    }

    @Override
    public void setLevelSetFunction(DifferentiableFunction levelSetFunction) {
        if (levelSetFunction.getOutputDimension() != 1) {
            throw new IllegalArgumentException("output should be 1d only, not" + levelSetFunction.getOutputDimension());
        }
        if (levelSetFunction.getInputDimension() != 2) {
            throw new IllegalArgumentException("input should be 2d only, not" + levelSetFunction.getInputDimension());
        }
        this.levelSetFunction = levelSetFunction;
    }

    protected void setupFunctionData(TriangleContourCell cell) {
        for (AdaptiveCellEdge edge : cell) {
            ContourNode nd = (ContourNode) edge.getStart();
            double[] nodeValue = nd.getFunctionValue();
            if (null == nodeValue) {
                nd.setFunctionValue(levelSetFunction.value(edge.getStart().getCoord(), null));
            }
        }
        cell.updateStatus(0);
    }

    protected void prepareToGenContour() {
        for (TriangleContourCell cell : cells) {
            cell.setVisited(false);
            for (AdaptiveCellEdge edge : cell) {
                edge.getStart().setId(-1);
            }
        }
        int nodesNum = 0;
        for (TriangleContourCell cell : cells) {
            for (AdaptiveCellEdge edge : cell) {
                Node start = edge.getStart();

                if (start.getId() > -1) {
                    continue;
                }
                ContourNode cStart = (ContourNode) start;
                cStart.setFunctionValue(null);
                start.setId(nodesNum++);
            }
        }

        contourHeads.clear();
        openRingsHeads.clear();
        cellsIterator = cells.iterator();
    }

    @Override
    public List<Line2D> getContourHeads() {
        return contourHeads;
    }

    protected TriangleContourCell nextUnvisitedCellWithContour() {
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

    public static double[] genLinearInterpolateContourPoint(Line2D contourSourceEdge) {
        double[] startCoord = contourSourceEdge.getStart().getCoord();
        double[] endCoord = contourSourceEdge.getEnd().getCoord();
        double t = genLinearInterpolateParameter(contourSourceEdge);
        double[] resultCoord = Math2D.pointOnSegment(startCoord, endCoord, t, null);
        return resultCoord;
    }

    public static double genLinearInterpolateParameter(Line2D contourSourceEdge) {
        double startValue = ((ContourNode) contourSourceEdge.getStart()).getFunctionValue()[0];
        double endValue = ((ContourNode) contourSourceEdge.getEnd()).getFunctionValue()[0];
        double t = startValue / (startValue - endValue);
        return t;
    }

    protected class OnLineFunction implements DifferentiableFunction {

        double[] endCoord;
        double[] startCoord;
        double[] coord = new double[2];
        double[] levelSetFuncVal;

        public void prepareToSolve(double[] startCoord, double[] endCoord) {
            this.endCoord = endCoord;
            this.startCoord = startCoord;
            levelSetFuncVal = null;
            coord = new double[2];
        }

        @Override
        public int getInputDimension() {
            return 1;
        }

        @Override
        public int getOutputDimension() {
            return 1;
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (null == output) {
                output = new double[getDiffOrder() + 1];
            }
            double t = input[0];
            double x = startCoord[0] * (1 - t) + endCoord[0] * t;
            double y = startCoord[1] * (1 - t) + endCoord[1] * t;
            coord[0] = x;
            coord[1] = y;
            levelSetFuncVal = levelSetFunction.value(coord, levelSetFuncVal);
            output[0] = levelSetFuncVal[0];
            if (getDiffOrder() >= 1) {
                double x_t = endCoord[0] - startCoord[0];
                double y_t = endCoord[1] - startCoord[1];
                output[1] = levelSetFuncVal[1] * x_t + levelSetFuncVal[2] * y_t;
            }
            return output;
        }

        @Override
        public int getDiffOrder() {
            return levelSetFunction.getDiffOrder();
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            levelSetFunction.setDiffOrder(diffOrder);
        }
    }
}
