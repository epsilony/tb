/* (c) Copyright by Man YUAN */
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
