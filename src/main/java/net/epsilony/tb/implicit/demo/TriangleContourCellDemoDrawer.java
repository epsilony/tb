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

package net.epsilony.tb.implicit.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import net.epsilony.tb.implicit.ContourNode;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.implicit.TriangleContourCell;
import net.epsilony.tb.solid.winged.WingedEdge;
import net.epsilony.tb.solid.ui.NodeDrawer;
import net.epsilony.tb.ui.ModelDrawerAdapter;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellDemoDrawer extends ModelDrawerAdapter {

    public static Color DEFAULT_VISITIED_FILLING = null;
    public static Color DEFAULT_UNVISITIED_FILLING = Color.CYAN;
    public static Color DEFAULT_NODE_BELOW_LEVEL = Color.RED;
    public static Color DEFAULT_NODE_ABOVE_LEVEL = Color.GREEN;
    public static Color DEFAULT_SEGMENT_COLOR = Color.LIGHT_GRAY;
    public static Color DEFAULT_NODE_NULL_DATA_COLOR = Color.YELLOW;
    Color visitedFilling = DEFAULT_VISITIED_FILLING;
    Color unvisitedFilling = DEFAULT_UNVISITIED_FILLING;
    Color below = DEFAULT_NODE_BELOW_LEVEL;
    Color above = DEFAULT_NODE_ABOVE_LEVEL;
    Color segmentColor = DEFAULT_SEGMENT_COLOR;
    TriangleContourCell cell;
    double value;
    Path2D path;
    NodeDrawer nodeDrawer = new NodeDrawer();
    Color nodeDataNullColor = DEFAULT_NODE_NULL_DATA_COLOR;
    boolean nodesVisible;

    public TriangleContourCellDemoDrawer(TriangleContourCell cell) {
        this.cell = cell;
        genSegmentsPathInModelSpace();
    }

    @Override
    public Rectangle2D getBoundsInModelSpace() {
        return path.getBounds2D();
    }

    @Override
    public void setModelToComponentTransform(AffineTransform modelToComponentTransform) {
        super.setModelToComponentTransform(modelToComponentTransform);
        nodeDrawer.setModelToComponentTransform(modelToComponentTransform);
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        nodeDrawer.setComponent(component);
    }

    @Override
    public void drawModel(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(segmentColor);
        Shape pathOnScreen = modelToComponentTransform.createTransformedShape(path);
        g2.draw(pathOnScreen);

        Color fillingColor = cell.isVisited() ? visitedFilling : unvisitedFilling;
        if (null != fillingColor) {
            g2.setColor(fillingColor);
            g2.fill(pathOnScreen);
        }

        if (nodesVisible) {
            for (WingedEdge edge : cell) {
                nodeDrawer.setNode(edge.getStart());
                Node node = edge.getStart();
                double[] funcValue = ((ContourNode) node).getFunctionValue();
                if (null == funcValue) {
                    continue;
                } else if (funcValue[0] < value) {
                    nodeDrawer.setColor(below);
                } else {
                    nodeDrawer.setColor(above);
                }
                nodeDrawer.drawModel(g2);
            }
        }
    }

    private void genSegmentsPathInModelSpace() {
        path = new Path2D.Double();

        Iterator<WingedEdge> iterator = cell.iterator();

        double[] coord = iterator.next().getStart().getCoord();
        path.moveTo(coord[0], coord[1]);
        while (iterator.hasNext()) {
            coord = iterator.next().getStart().getCoord();
            path.lineTo(coord[0], coord[1]);
        }
        path.closePath();
    }

    public boolean isNodesVisible() {
        return nodesVisible;
    }

    public void setNodesVisible(boolean nodesVisible) {
        this.nodesVisible = nodesVisible;
    }
}
