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

package net.epsilony.tb.ui.select.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.epsilony.tb.ui.UIUtils;
import net.epsilony.tb.ui.select.RectangleMouseSelector;
import net.epsilony.tb.ui.select.RectangleSelectionEvent;
import net.epsilony.tb.ui.select.RectangleSelectionListener;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RectangleMouseSelectorDemo {

    static class MyPanel extends JPanel implements RectangleSelectionListener {

        static final Color CANDIDATE_COLOR = Color.BLUE;
        static final Color SELECTED_COLOR = Color.RED;
        Rectangle2D rectangle = new Rectangle2D.Double();
        Color color;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            UIUtils.tidyRectangle2D(rectangle, rectangle);
            g2.draw(rectangle);

        }

        @Override
        public void candidateRectangleSelected(RectangleSelectionEvent e) {
            setRectangleAndRepaint(e);
            color = CANDIDATE_COLOR;
        }

        @Override
        public void rectangleSelected(RectangleSelectionEvent e) {
            setRectangleAndRepaint(e);
            color = SELECTED_COLOR;
        }

        private void setRectangleAndRepaint(RectangleSelectionEvent e) {
            Rectangle2D rc = new Rectangle2D.Double();
            rc.setRect(rectangle);
            rectangle = e.getRectangle();
            UIUtils.tidyRectangle2D(rectangle, rectangle);
            Rectangle2D.union(rc, rectangle, rc);
            UIUtils.repaintRectangle2D(this, rc);
        }
    }

    public static void createUI() {
        RectangleMouseSelector mouseSelector = new RectangleMouseSelector();
        MyPanel panel = new MyPanel();
        mouseSelector.addRectangleSelectionListener(panel);
        panel.addMouseListener(mouseSelector);
        panel.addMouseMotionListener(mouseSelector);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(800, 600);
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
