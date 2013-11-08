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

package net.epsilony.tb.ui;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class CommonFrame extends JFrame {

    BasicModelPanel basicModelPanel;

    public CommonFrame() {
        this(new BasicModelPanel());
    }

    public CommonFrame(BasicModelPanel basicModelPanelUI) {
        super();
        this.basicModelPanel = basicModelPanelUI;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(basicModelPanelUI);
        basicModelPanelUI.setPreferredSize(new Dimension(400, 300));
    }

    public BasicModelPanel getMainPanel() {
        return basicModelPanel;
    }

    public void setDefaultModelOriginAndScale(double originX, double originY, double scale) {
        basicModelPanel.setDefaultModelOriginAndScale(originX, originY, scale);
    }

    @Override
    public void setVisible(boolean visible) {
        pack();
        basicModelPanel.setZoomAllNeeded(true);
        super.setVisible(visible);
    }
}
