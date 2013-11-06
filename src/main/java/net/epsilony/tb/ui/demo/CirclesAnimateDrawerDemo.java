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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.epsilony.tb.ui.AnimateModelDrawer;
import net.epsilony.tb.ui.AnimationStatus;
import net.epsilony.tb.ui.CommonFrame;
import net.epsilony.tb.ui.ModelDrawer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class CirclesAnimateDrawerDemo {

    public static void createUI() {
        final double rad = 30;
        final CommonFrame frame = new CommonFrame();
        frame.setSize(800, 600);
        frame.getMainPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Point2D pt = new Point2D.Double(e.getX(), e.getY());
                        try {
                            frame.getMainPanel().getModelToComponentTransform().inverseTransform(pt, pt);
                        } catch (NoninvertibleTransformException ex) {
                            Logger.getLogger(CirclesAnimateDrawerDemo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        CircleAnimateDrawer animateDrawer = new CircleAnimateDrawer(pt.getX(), pt.getY(), rad);
                        frame.getMainPanel().addAndSetupModelDrawer(animateDrawer);
                        animateDrawer.switchStatus(AnimationStatus.APPEARING);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    List<ModelDrawer> modelDrawers = frame.getMainPanel().getModelDrawers();
                    for (ModelDrawer md : modelDrawers) {
                        if (md instanceof AnimateModelDrawer) {
                            AnimateModelDrawer amd = (AnimateModelDrawer) md;
                            amd.switchStatus(AnimationStatus.FADING);
                        }
                    }
                }
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createUI();
            }
        });
    }
}
