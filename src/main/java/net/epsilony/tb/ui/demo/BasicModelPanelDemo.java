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

package net.epsilony.tb.ui.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.ModelDrawerAdapter;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class BasicModelPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                creatDemoFrame();
            }
        });
    }

    public static void creatDemoFrame() {
        JFrame frame = new JFrame("OriginTransformListener");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BasicModelPanel myPanel = new BasicModelPanel();
        frame.getContentPane().add(myPanel);
        myPanel.setPreferredSize(new Dimension(800, 600));
        myPanel.addAndSetupModelDrawer(new ModelDrawerAdapter() {
            Rectangle2D rect = new Rectangle2D.Double(0, 0, 100, 50);

            @Override
            public void drawModel(Graphics2D g2) {
                g2.setColor(Color.BLACK);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.draw(getModelToComponentTransform().createTransformedShape(rect));
            }

            @Override
            public Rectangle2D getBoundsInModelSpace() {
                return rect;
            }
        });
        frame.pack();

        myPanel.setZoomAllNeeded(true);
        frame.setVisible(true);
    }
}
