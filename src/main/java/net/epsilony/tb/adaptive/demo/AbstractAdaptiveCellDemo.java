/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JCheckBox;
import net.epsilony.tb.adaptive.AdaptiveCell;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.ui.NodeDrawer;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.CommonFrame;
import net.epsilony.tb.ui.ModelDrawer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractAdaptiveCellDemo {

    public void createDemoUI() {
        CommonFrame frame = new CommonFrame();
        List<AdaptiveCell<Node>> cells = genCells();
        final BasicModelPanel mainPanel = frame.getMainPanel();
        Color nodeColor = NodeDrawer.DEFAULT_COLOR;
        nodeColor = new Color(nodeColor.getRed(), nodeColor.getGreen(), nodeColor.getBlue(), nodeColor.getAlpha() / 4);
        NodeDrawer.DEFAULT_COLOR = nodeColor;
        List<ModelDrawer> extraDrawers = getExtraDrawers();
        if (null != extraDrawers) {
            for (ModelDrawer dr : extraDrawers) {
                mainPanel.addAndSetupModelDrawer(dr);
            }
        }
        for (AdaptiveCell<Node> cell : cells) {
            AdaptiveCellDemoDrawer drawer = new AdaptiveCellDemoDrawer();
            drawer.setCell(cell);
            mainPanel.addAndSetupModelDrawer(drawer);
        }
        JCheckBox recursiveBox = new JCheckBox("recursively", true);
        final JCheckBox showOppositesBox = new JCheckBox("opposites", true);
        showOppositesBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdaptiveCellDemoDrawer.showOppositeMarks = showOppositesBox.isSelected();
                mainPanel.repaint();
            }
        });
        ClickToFission clickToFission = new ClickToFission(mainPanel, recursiveBox);
        frame.getContentPane().add(showOppositesBox);
        frame.getContentPane().add(recursiveBox);
        frame.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    protected abstract List<AdaptiveCell<Node>> genCells();

    protected List<ModelDrawer> getExtraDrawers() {
        return null;
    }
}
