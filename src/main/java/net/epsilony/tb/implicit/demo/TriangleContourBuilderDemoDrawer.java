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
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.implicit.TriangleContourBuilder;
import net.epsilony.tb.implicit.TriangleContourCell;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.ui.ModelDrawer;
import net.epsilony.tb.ui.ModelDrawerAdapter;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourBuilderDemoDrawer extends ModelDrawerAdapter {

    public static Color DEFAULT_CONTOUR_COLOR = Color.PINK;
    public static boolean DEFAULT_TRIANGLE_NODES_VISIBLE = true;
    TriangleContourBuilder trianglePolygonizer;
    List<TriangleContourCellDemoDrawer> triangleDrawers;
    ContourNodeDrawer nodeDrawer = new ContourNodeDrawer();
    boolean triangleNodesVisible = DEFAULT_TRIANGLE_NODES_VISIBLE;

    public TriangleContourBuilderDemoDrawer(TriangleContourBuilder trianglePolygonizer) {
        this.trianglePolygonizer = trianglePolygonizer;
        genDrawers();
    }

    @Override
    public Rectangle2D getBoundsInModelSpace() {
        Rectangle2D rect = triangleDrawers.get(0).getBoundsInModelSpace();
        for (ModelDrawer drawer : triangleDrawers) {
            Rectangle2D.union(rect, drawer.getBoundsInModelSpace(), rect);
        }
        return rect;
    }

    @Override
    public void drawModel(Graphics2D g2) {
        for (ModelDrawer drawer : triangleDrawers) {
            drawer.drawModel(g2);
        }

        drawContourNodes(g2);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(DEFAULT_CONTOUR_COLOR);
        g2.draw(modelToComponentTransform.createTransformedShape(genContourPath()));
    }

    private void drawContourNodes(Graphics2D g2) {
        for (Line chainHead : trianglePolygonizer.getContourHeads()) {
            nodeDrawer.setColor(DEFAULT_CONTOUR_COLOR);
            nodeDrawer.setNode(chainHead.getStart());
            nodeDrawer.drawModel(g2);
            Line seg = (Line) chainHead.getSucc();
            while (seg != null && seg != chainHead) {
                nodeDrawer.setNode(seg.getStart());
                nodeDrawer.drawModel(g2);
                seg = (Line) seg.getSucc();
            }
        }
    }

    private Path2D genContourPath() {
        Path2D path = Segment2DUtils.genChordPath(trianglePolygonizer.getContourHeads());
        return path;
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        for (ModelDrawer drawer : triangleDrawers) {
            drawer.setComponent(component);
        }
        nodeDrawer.setComponent(component);
    }

    @Override
    public void setModelToComponentTransform(AffineTransform modelToComponentTransform) {
        super.setModelToComponentTransform(modelToComponentTransform);
        for (ModelDrawer drawer : triangleDrawers) {
            drawer.setModelToComponentTransform(modelToComponentTransform);
        }
        nodeDrawer.setModelToComponentTransform(modelToComponentTransform);
    }

    private void genDrawers() {
        triangleDrawers = new LinkedList<>();
        for (TriangleContourCell cell : trianglePolygonizer.getCells()) {
            TriangleContourCellDemoDrawer cellDrawer = new TriangleContourCellDemoDrawer(cell);
            triangleDrawers.add(cellDrawer);
            cellDrawer.setNodesVisible(triangleNodesVisible);
        }
    }

    public boolean isTriangleNodesVisible() {
        return triangleNodesVisible;
    }

    public void setTriangleNodesVisible(boolean triangleNodesVisible) {
        this.triangleNodesVisible = triangleNodesVisible;
        for (TriangleContourCellDemoDrawer cellDrawer : triangleDrawers) {
            cellDrawer.setNodesVisible(triangleNodesVisible);
        }
    }
}
