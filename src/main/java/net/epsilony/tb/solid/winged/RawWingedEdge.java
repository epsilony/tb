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

import net.epsilony.tb.solid.RawSegment;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RawWingedEdge extends RawSegment implements WingedEdge {

    protected WingedEdge opposite;
    protected WingedCell cell;

    @Override
    public WingedEdge getOpposite() {
        return opposite;
    }

    @Override
    public void setOpposite(WingedEdge opposite) {
        this.opposite = opposite;
    }

    @Override
    public WingedCell getCell() {
        return cell;
    }

    @Override
    public void setCell(WingedCell cell) {
        this.cell = cell;
    }
}
