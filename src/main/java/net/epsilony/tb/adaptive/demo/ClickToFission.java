/* (c) Copyright by Man YUAN */
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
import net.epsilony.tb.solid.Node;
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
                AdaptiveCell<Node> cell = quadDrawer.getCell();
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

    private void fission(AdaptiveCell<Node> cell, Collection<ModelDrawer> newDrawers) {
        LinkedList<AdaptiveCell<Node>> newCells = new LinkedList<>();
        AdaptiveUtils.fission(cell, recursivelyFissionCheckBox.isSelected(), newCells);
        
        for (AdaptiveCell newCell : newCells) {
            AdaptiveCellDemoDrawer drawer = new AdaptiveCellDemoDrawer();
            drawer.setCell(newCell);
            newDrawers.add(drawer);
        }
    }
}
