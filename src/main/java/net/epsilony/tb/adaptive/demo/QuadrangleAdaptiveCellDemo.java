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

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.epsilony.tb.adaptive.QuadrangleAdaptiveCellFactory;
import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.WingedCell;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCellDemo extends AbstractAdaptiveCellDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuadrangleAdaptiveCellDemo().createDemoUI();
            }
        });
    }

    @Override
    protected List<WingedCell> genCells() {
        double[] xs = TestTool.linSpace(0, 200, 10);
        double[] ys = TestTool.linSpace(100, 0, 5);
        QuadrangleAdaptiveCellFactory factory = new QuadrangleAdaptiveCellFactory();
        factory.setNodeClass(Node.class);
        factory.setXs(xs);
        factory.setYs(ys);
        return new ArrayList<WingedCell>(factory.produce());

    }
}
