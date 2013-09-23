/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.WingedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCellFactory implements Factory<List<QuadrangleAdaptiveCell>> {

    public static Logger logger = LoggerFactory.getLogger(QuadrangleAdaptiveCellFactory.class);
    double[] xs;
    double[] ys;
    Class<? extends Node> nodeClass;   //to ensure that ND has null constructor, so factory pattern is not a good choice

    public double[] getXs() {
        return xs;
    }

    public void setXs(double[] xs) {
        this.xs = xs;
    }

    public double[] getYs() {
        return ys;
    }

    public void setYs(double[] ys) {
        this.ys = ys;
    }

    public Class getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class nodeClass) {
        this.nodeClass = nodeClass;
    }

    private ArrayList<ArrayList<QuadrangleAdaptiveCell>> byCoordGrid(ArrayList<ArrayList<Node>> nodeGrid) {

        int numRow = ys.length - 1;
        int numCol = xs.length - 1;
        ArrayList<ArrayList<QuadrangleAdaptiveCell>> result = new ArrayList<>(numRow);

        for (int rowIndex = 0; rowIndex < numRow; rowIndex++) {
            ArrayList<QuadrangleAdaptiveCell> resultRow = new ArrayList<>(numCol);
            result.add(resultRow);
            for (int colIndex = 0; colIndex < numCol; colIndex++) {
                resultRow.add(newCell(Arrays.asList(
                        nodeGrid.get(rowIndex + 1).get(colIndex),
                        nodeGrid.get(rowIndex + 1).get(colIndex + 1),
                        nodeGrid.get(rowIndex).get(colIndex + 1),
                        nodeGrid.get(rowIndex).get(colIndex))));
            }
        }

        return result;
    }

    private void linkCellGridOpposites(ArrayList<ArrayList<QuadrangleAdaptiveCell>> cellGrid) {
        for (int i = 0; i < cellGrid.size() - 1; i++) {
            ArrayList<QuadrangleAdaptiveCell> gridRow = cellGrid.get(i);
            ArrayList<QuadrangleAdaptiveCell> nextRow = cellGrid.get(i + 1);
            for (int j = 0; j < gridRow.size(); j++) {
                WingedUtils.linkAsOpposite(gridRow.get(j).getVertexEdge(0), nextRow.get(j).getVertexEdge(2));
            }
        }
        for (int i = 0; i < cellGrid.size(); i++) {
            ArrayList<QuadrangleAdaptiveCell> gridRow = cellGrid.get(i);
            for (int j = 0; j < gridRow.size() - 1; j++) {
                WingedUtils.linkAsOpposite(gridRow.get(j).getVertexEdge(1), gridRow.get(j + 1).getVertexEdge(3));
            }
        }
    }

    private ArrayList<ArrayList<Node>> xysToNodes() {
        ArrayList<ArrayList<Node>> result = new ArrayList<>(ys.length);
        for (int i = 0; i < ys.length; i++) {
            ArrayList row = new ArrayList<>(xs.length);
            result.add(row);
            for (int j = 0; j < xs.length; j++) {
                Node newNode = newNode();
                newNode.setCoord(new double[]{xs[j], ys[i]});
                row.add(newNode);
            }
        }
        return result;
    }

    private QuadrangleAdaptiveCell newCell(List<Node> counterClockwiseVetes) {
        if (counterClockwiseVetes.size() != 4) {
            throw new IllegalArgumentException();
        }
        QuadrangleAdaptiveCell result = new QuadrangleAdaptiveCell();
        for (int i = 0; i < counterClockwiseVetes.size(); i++) {
            result.setVertexEdge(i, new AdaptiveCellEdge());
            result.setVertex(i, counterClockwiseVetes.get(i));
        }
        WingedUtils.linkCornerEdges(result);
        WingedUtils.linkEdgeAndCell(result);
        return result;
    }

    @Override
    public List<QuadrangleAdaptiveCell> produce() {
        ArrayList<ArrayList<Node>> nodeGrid = xysToNodes();
        ArrayList<ArrayList<QuadrangleAdaptiveCell>> cellGrid = byCoordGrid(nodeGrid);
        linkCellGridOpposites(cellGrid);
        List<QuadrangleAdaptiveCell> result = new LinkedList<>();
        for (List<QuadrangleAdaptiveCell> row : cellGrid) {
            result.addAll(row);
        }
        return result;
    }

    private Node newNode() {
        try {
            return nodeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("ND does not hava a null constructor", ex);
            throw new IllegalStateException(ex);
        }
    }
}
