/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.ui;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
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

    public static GeneralPath genGeneralPath(Facet polygon) {
        GeneralPath path = new GeneralPath();
        ArrayList<Segment> chainsHeads = polygon.getChainsHeads();
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
