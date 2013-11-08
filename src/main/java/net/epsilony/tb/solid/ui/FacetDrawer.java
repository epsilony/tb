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

package net.epsilony.tb.solid.ui;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.List;
import javax.swing.SwingUtilities;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Facet;
import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Segment;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.CommonFrame;
import net.epsilony.tb.ui.SingleModelShapeDrawer;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class FacetDrawer extends SingleModelShapeDrawer {

    Facet polygon;

    public static GeneralPath genGeneralPath(Facet facet) {
        GeneralPath path = new GeneralPath();
        List<Segment> chainsHeads = facet.getRingsHeads();
        for (Segment chainHead : chainsHeads) {
            Node nd = chainHead.getStart();
            path.moveTo(nd.getCoord()[0], nd.getCoord()[1]);

            Segment seg = chainHead;
            do {
                seg = seg.getSucc();
                nd = seg.getStart();
                path.lineTo(nd.getCoord()[0], nd.getCoord()[1]);
            } while (seg != chainHead);
            path.closePath();
        }
        return path;
    }

    public FacetDrawer() {
    }

    public FacetDrawer(Facet polygon) {
        _setPolygon(polygon);
    }

    public void setPolygon(Facet polygon) {
        _setPolygon(polygon);
    }

    private void _setPolygon(Facet polygon) {
        this.polygon = polygon;
        Shape polygonPath = genGeneralPath(polygon);
        _setShape(polygonPath);
    }

    public static void main(String[] args) {
        Runnable createUI = new Runnable() {
            @Override
            public void run() {
                CommonFrame frame = new CommonFrame();
                BasicModelPanel basicModelPanel = frame.getMainPanel();
                basicModelPanel.setPreferredSize(new Dimension(800, 600));
                Facet polygon = TestTool.samplePolygon(null);
                FacetDrawer drawer = new FacetDrawer();
                drawer.setPolygon(polygon);
                basicModelPanel.addAndSetupModelDrawer(drawer);
                frame.setVisible(true);
            }
        };

        SwingUtilities.invokeLater(createUI);
    }
}
