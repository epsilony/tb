/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.IntIdentityMap;
import net.epsilony.tb.adaptive.AdaptiveCellEdge;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractTriangleContourBuilder {

    public static double DEFAULT_CONTOUR_LEVEL = 0;
    protected List<TriangleContourCell> cells;
    protected double contourLevel = DEFAULT_CONTOUR_LEVEL;
    protected DifferentiableFunction<double[], double[]> levelSetFunction;
    NewtonSolver newtonSolver = null;
    IntIdentityMap<Node, double[]> nodesValuesMap = new IntIdentityMap<>();

    public List<TriangleContourCell> getCells() {
        return cells;
    }

    public DifferentiableFunction<double[], double[]> getLevelSetFunction() {
        return levelSetFunction;
    }

    public NewtonSolver getNewtonSolver() {
        return newtonSolver;
    }

    public IntIdentityMap<Node, double[]> getNodesValuesMap() {
        return nodesValuesMap;
    }

    public void setCells(List<TriangleContourCell> cells) {
        this.cells = cells;
    }

    public void setContourLevel(double contourLevel) {
        this.contourLevel = contourLevel;
    }

    public void setLevelSetFunction(DifferentiableFunction<double[], double[]> levelSetFunction) {
        if (levelSetFunction.getOutputDimension() != 1) {
            throw new IllegalArgumentException("output should be 1d only, not" + levelSetFunction.getOutputDimension());
        }
        if (levelSetFunction.getInputDimension() != 2) {
            throw new IllegalArgumentException("input should be 2d only, not" + levelSetFunction.getInputDimension());
        }
        this.levelSetFunction = levelSetFunction;
    }

    public void setNewtonSolver(NewtonSolver newtonSolver) {
        this.newtonSolver = newtonSolver;
        newtonSolver.setFunction(levelSetFunction);
    }

    protected void setupFunctionData(TriangleContourCell cell) {
        for (AdaptiveCellEdge edge : cell) {
            Node nd = edge.getStart();
            double[] nodeValue = nodesValuesMap.get(nd);
            if (null == nodeValue) {
                nodesValuesMap.put(nd, levelSetFunction.value(edge.getStart().getCoord(), null));
            }
        }
        cell.updateStatus(contourLevel, nodesValuesMap);
    }

    public double getContourLevel() {
        return contourLevel;
    }

    protected void prepareCellAndNodes() {
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
                start.setId(nodesNum++);
            }
        }
        nodesValuesMap.clear();
        nodesValuesMap.appendNullValues(nodesNum);
    }
}
