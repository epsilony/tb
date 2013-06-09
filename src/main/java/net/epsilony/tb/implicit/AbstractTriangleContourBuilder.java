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

<<<<<<< Updated upstream
    public static double DEFAULT_CONTOUR_LEVEL = 0;
=======
>>>>>>> Stashed changes
    protected List<TriangleContourCell> cells;
    protected double contourLevel = DEFAULT_CONTOUR_LEVEL;
    protected DifferentiableFunction<double[], double[]> levelSetFunction;
    protected NewtonSolver newtonSolver = null;
    protected List<Line2D> contourHeads = new LinkedList<>();
    protected Iterator<TriangleContourCell> cellsIterator;
    protected List<Line2D> openRingsHeads = new LinkedList<>();

    @Override
    public List<TriangleContourCell> getCells() {
        return cells;
    }

    @Override
    public DifferentiableFunction<double[], double[]> getLevelSetFunction() {
        return levelSetFunction;
    }

    @Override
    public NewtonSolver getNewtonSolver() {
        return newtonSolver;
    }

    @Override
    public void setCells(List<TriangleContourCell> cells) {
        this.cells = cells;
    }

    @Override
    public void setContourLevel(double contourLevel) {
        this.contourLevel = contourLevel;
    }

    @Override
    public void setLevelSetFunction(DifferentiableFunction<double[], double[]> levelSetFunction) {
        if (levelSetFunction.getOutputDimension() != 1) {
            throw new IllegalArgumentException("output should be 1d only, not" + levelSetFunction.getOutputDimension());
        }
        if (levelSetFunction.getInputDimension() != 2) {
            throw new IllegalArgumentException("input should be 2d only, not" + levelSetFunction.getInputDimension());
        }
        this.levelSetFunction = levelSetFunction;
    }

    @Override
    public void setNewtonSolver(NewtonSolver newtonSolver) {
        this.newtonSolver = newtonSolver;
        if (null != newtonSolver) {
            newtonSolver.setFunction(levelSetFunction);
        }
    }

    protected void setupFunctionData(TriangleContourCell cell) {
        for (AdaptiveCellEdge edge : cell) {
            ContourNode nd = (ContourNode) edge.getStart();
            double[] nodeValue = nd.getFunctionValue();
            if (null == nodeValue) {
                nd.setFunctionValue(levelSetFunction.value(edge.getStart().getCoord(), null));
            }
        }
        cell.updateStatus(contourLevel);
    }

    @Override
    public double getContourLevel() {
        return contourLevel;
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
                if (!(start instanceof ContourNode)) {
                    ContourNode newNode = new ContourNode();
                    newNode.setCoord(start.getCoord());
                    edge.setStart(newNode);
                    start = newNode;
                } else {
                    ContourNode cStart = (ContourNode) start;
                    cStart.setFunctionValue(null);
                }
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
        double startValue = ((ContourNode) contourSourceEdge.getStart()).getFunctionValue()[0];
        double endValue = ((ContourNode) contourSourceEdge.getEnd()).getFunctionValue()[0];
        double t = startValue / (startValue - endValue);
        double[] resultCoord = Math2D.pointOnSegment(startCoord, endCoord, t, null);
        return resultCoord;
    }
}
