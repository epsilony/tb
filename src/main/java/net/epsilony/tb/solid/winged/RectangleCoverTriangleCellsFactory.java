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
public class RectangleCoverTriangleCellsFactory implements Factory<List<WingedCell>> {

    GeneralTriangleCellFactory generalTriangleCellFactory = new GeneralTriangleCellFactory();
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

    public Factory<? extends WingedCell> getCellFactory() {
        return generalTriangleCellFactory.getCellFactory();
    }

    public void setCellFactory(Factory<? extends WingedCell> cellFactory) {
        generalTriangleCellFactory.setCellFactory(cellFactory);
    }

    public Factory<? extends WingedEdge> getEdgeFactory() {
        return generalTriangleCellFactory.getEdgeFactory();
    }

    public void setEdgeFactory(Factory<? extends WingedEdge> edgeFactory) {
        generalTriangleCellFactory.setEdgeFactory(edgeFactory);
    }

    public Factory<? extends Node> getNodeFactory() {
        return generalTriangleCellFactory.getNodeFactory();
    }

    public void setNodeFactory(Factory<? extends Node> nodeFactory) {
        generalTriangleCellFactory.setNodeFactory(nodeFactory);
    }

    @Override
    public List<WingedCell> produce() {
        List<WingedCell> result = new LinkedList<>();
        ArrayList<ArrayList<WingedCell>> triangleGrid = genTriangleGridCoversRectangle();
        for (List<WingedCell> row : triangleGrid) {
            result.addAll(row);
        }
        return result;
    }

    public ArrayList<ArrayList<WingedCell>> genTriangleGridCoversRectangle() {
        rectangle = UIUtils.tidyRectangle2D(rectangle, null);
        ArrayList<ArrayList<Node>> nodesGrid = genNodesGrid();
        ArrayList<ArrayList<WingedCell>> cellGrid = genCellGrid(nodesGrid);
        linkTrianglesOpposites(cellGrid);
        return cellGrid;
    }

    private ArrayList<ArrayList<Node>> genNodesGrid() {
        double[][][] coordsGrid = genVertesCoordsGrid();
        ArrayList<ArrayList<Node>> result = new ArrayList<>(coordsGrid.length);
        for (double[][] coordsRow : coordsGrid) {
            ArrayList<Node> nodeRow = new ArrayList<>(coordsRow.length);
            result.add(nodeRow);
            for (double[] coord : coordsRow) {
                Node node = generalTriangleCellFactory.nodeFactory.produce();
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
                double[] coord = new double[] { startX + edgeLength * j, y };
                row[j] = coord;
            }
        }
        return result;
    }

    private ArrayList<ArrayList<WingedCell>> genCellGrid(ArrayList<ArrayList<Node>> nodeGrid) {
        int numCols = (nodeGrid.get(0).size() - 1) * 2;
        int numRows = nodeGrid.size() - 1;
        ArrayList<ArrayList<WingedCell>> cellGrid = new ArrayList<>(numRows);
        for (int i = 0; i < numRows; i++) {
            int rowMod = i % 2;
            ArrayList<WingedCell> cellRow = new ArrayList<>(numCols);
            cellGrid.add(cellRow);
            generalTriangleCellFactory.setGenVertes(false);
            for (int j = 0; j < numCols; j++) {
                WingedCell cell = generalTriangleCellFactory.produce();
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

    private void linkTrianglesOpposites(ArrayList<ArrayList<WingedCell>> triangles) {
        for (int i = 0; i < triangles.size(); i++) {
            final int rowMod = i % 2;
            ArrayList<WingedCell> row = triangles.get(i);
            for (int j = 0; j < row.size() - 1; j++) {
                if (j % 2 == 0 && rowMod == 0 || j % 2 == 1 && rowMod == 1) {
                    WingedUtils.linkAsOpposite(row.get(j).getVertexEdge(0), row.get(j + 1).getVertexEdge(0));
                } else {
                    WingedUtils.linkAsOpposite(row.get(j).getVertexEdge(2), row.get(j + 1).getVertexEdge(2));
                }
            }
        }
        for (int i = 0; i < triangles.size() - 1; i++) {
            final int startJ = i % 2;
            ArrayList<WingedCell> row = triangles.get(i);
            ArrayList<WingedCell> nextRow = triangles.get(i + 1);
            for (int j = 0; j < row.size(); j += 2) {
                WingedUtils.linkAsOpposite(row.get(j + startJ).getVertexEdge(1),
                        nextRow.get(j + startJ).getVertexEdge(1));
            }
        }
    }
}
