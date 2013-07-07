/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.ui.UIUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RectangleCoverTriangleCellsFactory<
        CELL extends WingedCell<CELL, EDGE, NODE>, //
        EDGE extends WingedEdge<CELL, EDGE, NODE>, //
        NODE extends Node> implements Factory<List<CELL>> {

    GeneralTriangleCellFactory<CELL, EDGE, NODE> generalTriangleCellFactory = new GeneralTriangleCellFactory<>();
    Rectangle2D rectangle;
    double edgeLength;

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    public void setEdgeLength(double edgeLength) {
        this.edgeLength = edgeLength;
    }

    public Factory<? extends CELL> getCellFactory() {
        return generalTriangleCellFactory.getCellFactory();
    }

    public void setCellFactory(Factory<? extends CELL> cellFactory) {
        generalTriangleCellFactory.setCellFactory(cellFactory);
    }

    public Factory<? extends EDGE> getEdgeFactory() {
        return generalTriangleCellFactory.getEdgeFactory();
    }

    public void setEdgeFactory(Factory<? extends EDGE> edgeFactory) {
        generalTriangleCellFactory.setEdgeFactory(edgeFactory);
    }

    public Factory<? extends NODE> getNodeFactory() {
        return generalTriangleCellFactory.getNodeFactory();
    }

    public void setNodeFactory(Factory<? extends NODE> nodeFactory) {
        generalTriangleCellFactory.setNodeFactory(nodeFactory);
    }

    @Override
    public List<CELL> produce() {
        List<CELL> result = new LinkedList<>();
        ArrayList<ArrayList<CELL>> triangleGrid = genTriangleGridCoversRectangle();
        for (List<CELL> row : triangleGrid) {
            result.addAll(row);
        }
        return result;
    }

    public ArrayList<ArrayList<CELL>> genTriangleGridCoversRectangle() {
        rectangle = UIUtils.tidyRectangle2D(rectangle, null);
        ArrayList<ArrayList<NODE>> nodesGrid = genNodesGrid();
        ArrayList<ArrayList<CELL>> cellGrid = genCellGrid(nodesGrid);
        linkTrianglesOpposites(cellGrid);
        return cellGrid;
    }

    private ArrayList<ArrayList<NODE>> genNodesGrid() {
        double[][][] coordsGrid = genVertesCoordsGrid();
        ArrayList<ArrayList<NODE>> result = new ArrayList<>(coordsGrid.length);
        for (double[][] coordsRow : coordsGrid) {
            ArrayList<NODE> nodeRow = new ArrayList<>(coordsRow.length);
            result.add(nodeRow);
            for (double[] coord : coordsRow) {
                NODE node = generalTriangleCellFactory.nodeFactory.produce();
                node.setCoord(coord);
                nodeRow.add(node);
            }
        }
        return result;
    }

    private double[][][] genVertesCoordsGrid() {
        final double SQRT_3 = Math.sqrt(3);
        double x0 = rectangle.getX() - edgeLength / 4;
        double y0 = rectangle.getY() - edgeLength * SQRT_3 / 4;
        double t = (rectangle.getWidth() - edgeLength / 4) / edgeLength;
        int numCols = (int) Math.ceil(t) + 2;
        if (Math.ceil(t) == t) {
            numCols++;
        }
        t = (rectangle.getHeight() - SQRT_3 * edgeLength / 4) / (SQRT_3 / 2 * edgeLength);
        int numRows = (int) Math.ceil(t) + 2;
        if (Math.ceil(t) == t) {
            numRows++;
        }

        double[][][] result = new double[numRows][numCols][];
        for (int i = 0; i < numRows; i++) {
            double[][] row = result[i];

            double startX = x0 - 0.5 * edgeLength * (i % 2);
            double y = y0 + i * edgeLength * SQRT_3 / 2;
            for (int j = 0; j < numCols; j++) {
                double[] coord = new double[]{startX + edgeLength * j, y};
                row[j] = coord;
            }
        }
        return result;
    }

    private ArrayList<ArrayList<CELL>> genCellGrid(ArrayList<ArrayList<NODE>> nodeGrid) {
        int numCols = (nodeGrid.get(0).size() - 1) * 2;
        int numRows = nodeGrid.size() - 1;
        ArrayList<ArrayList<CELL>> cellGrid = new ArrayList<>(numRows);
        for (int i = 0; i < numRows; i++) {
            int rowMod = i % 2;
            ArrayList<CELL> cellRow = new ArrayList<>(numCols);
            cellGrid.add(cellRow);
            generalTriangleCellFactory.setGenVertes(false);
            for (int j = 0; j < numCols; j++) {
                CELL cell = generalTriangleCellFactory.produce();
                cellRow.add(cell);
                if (rowMod == 0 && j % 2 == 0 || rowMod == 1 && j % 2 == 1) {
                    cell.setVertex(0, nodeGrid.get(i).get(j / 2 + rowMod));
                    cell.setVertex(1, nodeGrid.get(i + 1).get(j / 2 + 1));
                    cell.setVertex(2, nodeGrid.get(i + 1).get(j / 2));
                } else {
                    cell.setVertex(0, nodeGrid.get(i + 1).get(j / 2 + rowMod * -1 + 1));
                    cell.setVertex(1, nodeGrid.get(i).get(j / 2));
                    cell.setVertex(2, nodeGrid.get(i).get(j / 2 + 1));
                }
            }
        }
        return cellGrid;
    }

    private void linkTrianglesOpposites(ArrayList<ArrayList<CELL>> triangles) {
        for (int i = 0; i < triangles.size(); i++) {
            final int rowMod = i % 2;
            ArrayList<CELL> row = triangles.get(i);
            for (int j = 0; j < row.size() - 1; j++) {
                if (j % 2 == 0 && rowMod == 0 || j % 2 == 1 && rowMod == 1) {
                    WingedEdgeUtils.linkAsOpposite(row.get(j).getVertexEdge(0), row.get(j + 1).getVertexEdge(0));
                } else {
                    WingedEdgeUtils.linkAsOpposite(row.get(j).getVertexEdge(2), row.get(j + 1).getVertexEdge(2));
                }
            }
        }
        for (int i = 0; i < triangles.size() - 1; i++) {
            final int startJ = i % 2;
            ArrayList<CELL> row = triangles.get(i);
            ArrayList<CELL> nextRow = triangles.get(i + 1);
            for (int j = 0; j < row.size(); j += 2) {
                WingedEdgeUtils.linkAsOpposite(row.get(j + startJ).getVertexEdge(1), nextRow.get(j + startJ).getVertexEdge(1));
            }
        }
    }
}
