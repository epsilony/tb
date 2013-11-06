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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import net.epsilony.tb.adaptive.AdaptiveCell;
import net.epsilony.tb.adaptive.AdaptiveUtils;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.ModelDrawer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ClickToFission extends MouseAdapter {

    BasicModelPanel basicPanel;
    private final JCheckBox recursivelyFissionCheckBox;

    public ClickToFission(BasicModelPanel basicPanel, JCheckBox recursivelyFissionCheckBox) {
        prepare(basicPanel);
        this.recursivelyFissionCheckBox = recursivelyFissionCheckBox;
    }

    private void prepare(BasicModelPanel basicPanel) {
        this.basicPanel = basicPanel;
        basicPanel.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 1) {
            return;
        }
        List<ModelDrawer> modelDrawers = basicPanel.getModelDrawers();
        LinkedList<ModelDrawer> newDrawers = new LinkedList<>();
        for (ModelDrawer md : modelDrawers) {
            if (md instanceof AdaptiveCellDemoDrawer) {
                AdaptiveCellDemoDrawer quadDrawer = (AdaptiveCellDemoDrawer) md;
                AdaptiveCell cell = quadDrawer.getCell();
                try {
                    if (cell.getChildren() == null && quadDrawer.isComponentPointInside(e.getX(), e.getY())) {
                        fission(cell, newDrawers);
                    }
                } catch (NoninvertibleTransformException ex) {
                    Logger.getLogger(ClickToFission.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        for (ModelDrawer nd : newDrawers) {
            basicPanel.addAndSetupModelDrawer(nd);
        }
        if (newDrawers.size() > 0) {
            basicPanel.repaint();
        }
    }

    private void fission(AdaptiveCell cell, Collection<ModelDrawer> newDrawers) {
        LinkedList<AdaptiveCell> newCells = new LinkedList<>();
        AdaptiveUtils.fission(cell, recursivelyFissionCheckBox.isSelected(), newCells);

        for (AdaptiveCell newCell : newCells) {
            AdaptiveCellDemoDrawer drawer = new AdaptiveCellDemoDrawer();
            drawer.setCell(newCell);
            newDrawers.add(drawer);
        }
    }
}
