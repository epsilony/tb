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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JCheckBox;
import net.epsilony.tb.adaptive.AdaptiveCell;
import net.epsilony.tb.solid.ui.NodeDrawer;
import net.epsilony.tb.solid.winged.WingedCell;
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
        List<WingedCell> cells = genCells();
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
        for (WingedCell cell : cells) {
            AdaptiveCellDemoDrawer drawer = new AdaptiveCellDemoDrawer();
            drawer.setCell((AdaptiveCell) cell);
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

    protected abstract List<WingedCell> genCells();

    protected List<ModelDrawer> getExtraDrawers() {
        return null;
    }
}
