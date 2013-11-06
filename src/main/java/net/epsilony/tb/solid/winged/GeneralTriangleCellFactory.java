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

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class GeneralTriangleCellFactory implements Factory<WingedCell> {

    Factory<? extends WingedCell> cellFactory;
    Factory<? extends WingedEdge> edgeFactory;
    Factory<? extends Node> nodeFactory;
    boolean genVertes = true;

    public GeneralTriangleCellFactory(Factory<? extends TriangleCell> cellFactory, Factory<? extends WingedEdge> edgeFactory, Factory<? extends Node> nodeFactory) {
        this.cellFactory = cellFactory;
        this.edgeFactory = edgeFactory;
        this.nodeFactory = nodeFactory;
    }

    public GeneralTriangleCellFactory() {
    }

    @Override
    public WingedCell produce() {
        WingedCell result = cellFactory.produce();
        for (int i = 0; i < 3; i++) {
            result.setVertexEdge(i, edgeFactory.produce());
            if (genVertes) {
                result.setVertex(i, nodeFactory.produce());
            }
        }
        for (int i = 0; i < 3; i++) {
            Segment2DUtils.link(result.getVertexEdge(i), result.getVertexEdge((i + 1) % 3));
            result.getVertexEdge(i).setCell(result);
        }
        return result;
    }

    public boolean isGenVertes() {
        return genVertes;
    }

    public void setGenVertes(boolean genVertes) {
        this.genVertes = genVertes;
    }

    public Factory<? extends WingedCell> getCellFactory() {
        return cellFactory;
    }

    public void setCellFactory(Factory<? extends WingedCell> cellFactory) {
        this.cellFactory = cellFactory;
    }

    public Factory<? extends WingedEdge> getEdgeFactory() {
        return edgeFactory;
    }

    public void setEdgeFactory(Factory<? extends WingedEdge> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }

    public Factory<? extends Node> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(Factory<? extends Node> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
