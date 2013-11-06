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

package net.epsilony.tb.adaptive.demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import net.epsilony.tb.adaptive.TriangleAdaptiveCellFactory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.WingedCell;
import net.epsilony.tb.ui.ModelDrawer;
import net.epsilony.tb.ui.ModelDrawerAdapter;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCellDemo extends AbstractAdaptiveCellDemo {

    public static double TRIANGLE_EDGE_LENGTH = 20;
    public static Rectangle2D TRIANGLE_COVERY_RANGE = new Rectangle2D.Double(5, 5, 100, 60);

    @Override
    protected List<WingedCell> genCells() {
        TriangleAdaptiveCellFactory factory = new TriangleAdaptiveCellFactory();
        factory.setNodeClass(Node.class);
        factory.setRectangle(TRIANGLE_COVERY_RANGE);
        factory.setEdgeLength(TRIANGLE_EDGE_LENGTH);
        return factory.produce();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TriangleAdaptiveCellDemo().createDemoUI();
            }
        });
    }

    @Override
    protected List<ModelDrawer> getExtraDrawers() {
        ModelDrawer drawer = new ModelDrawerAdapter() {
            @Override
            public Rectangle2D getBoundsInModelSpace() {
                return null;
            }

            @Override
            public void drawModel(Graphics2D g2) {
                g2.setColor(Color.GRAY);
                g2.draw(modelToComponentTransform.createTransformedShape(TRIANGLE_COVERY_RANGE));
            }
        };

        return Arrays.asList(drawer);
    }
}
