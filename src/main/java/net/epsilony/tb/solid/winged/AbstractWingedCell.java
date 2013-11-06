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

package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractWingedCell implements WingedCell {

    ArrayList<WingedEdge> vertesEdges = new ArrayList<>(getNumberOfVertes());

    protected AbstractWingedCell() {
        for (int i = 0; i < getNumberOfVertes(); i++) {
            vertesEdges.add(null);
        }
    }

    @Override
    public WingedEdge getVertexEdge(int i) {
        return vertesEdges.get(i);
    }

    @Override
    public void setVertexEdge(int i, WingedEdge edge) {
        vertesEdges.set(i, edge);
    }

    @Override
    public void setVertex(int index, Node vertex) {
        getVertexEdge(index).setStart(vertex);
    }

    @Override
    public Node getVertex(int index) {
        return getVertexEdge(index).getStart();
    }
}
