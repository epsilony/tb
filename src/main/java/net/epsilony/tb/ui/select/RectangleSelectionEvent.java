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

package net.epsilony.tb.ui.select;

import java.awt.geom.Rectangle2D;
import java.util.EventObject;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RectangleSelectionEvent extends EventObject {

    private final Rectangle2D rectangle;
    private final boolean keepFormerSelections;

    public RectangleSelectionEvent(Object source, Rectangle2D rectangle, boolean keepFormerSelections) {
        super(source);
        this.rectangle = rectangle;
        this.keepFormerSelections = keepFormerSelections;
    }

    public boolean isKeepFormerSelections() {
        return keepFormerSelections;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }
}
