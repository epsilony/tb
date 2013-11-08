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

package net.epsilony.tb.adaptive;

import net.epsilony.tb.RudeFactory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.RectangleCoverTriangleCellsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCellFactory extends RectangleCoverTriangleCellsFactory {

    public static Logger logger = LoggerFactory.getLogger(TriangleAdaptiveCellFactory.class);
    Class<? extends Node> nodeClass;

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
        setNodeFactory(new RudeFactory<>(this.nodeClass));
    }

    public TriangleAdaptiveCellFactory() {
        setCellFactory(new RudeFactory<>(TriangleAdaptiveCell.class));
        setEdgeFactory(new RudeFactory<>(AdaptiveCellEdge.class));
    }
}
