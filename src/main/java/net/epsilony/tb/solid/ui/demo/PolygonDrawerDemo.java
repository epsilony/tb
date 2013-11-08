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

package net.epsilony.tb.solid.ui.demo;

import javax.swing.SwingUtilities;
import net.epsilony.tb.solid.Facet;
import net.epsilony.tb.solid.ui.NodeDrawer;
import net.epsilony.tb.solid.ui.FacetDrawer;
import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Segment;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.CommonFrame;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class PolygonDrawerDemo {

    public static void createUI() {
        Facet polygon = TestTool.samplePolygon(null);
        FacetDrawer polygonDrawer = new FacetDrawer(polygon);
        CommonFrame frame = new CommonFrame();
        BasicModelPanel mainPanel = frame.getMainPanel();
        mainPanel.addAndSetupModelDrawer(polygonDrawer);
        for (Segment seg : polygon) {
            mainPanel.addAndSetupModelDrawer(new NodeDrawer(seg.getStart()));
        }
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
