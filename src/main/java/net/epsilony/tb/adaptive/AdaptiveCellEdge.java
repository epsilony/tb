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

import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.RawWingedEdge;
import net.epsilony.tb.solid.winged.WingedEdge;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveCellEdge extends RawWingedEdge {

    @Override
    public void bisect() {
        AdaptiveCellEdge newSucc = new AdaptiveCellEdge();
        Node newNode = Node.instanceByClass(getStart());
        newNode.setCoord(Segment2DUtils.chordMidPoint(this, null));
        newSucc.setStart(newNode);
        newSucc.setSucc(getSucc());
        newSucc.setPred(this);
        getSucc().setPred(newSucc);
        setSucc(newSucc);
        newSucc.setCell(cell);

        if (null == opposite) {
            return;
        }

        AdaptiveCellEdge newOpposite = new AdaptiveCellEdge();
        newOpposite.setStart(getEnd());
        newOpposite.setSucc(opposite.getSucc());
        newOpposite.setPred(opposite);
        opposite.getSucc().setPred(newOpposite);
        opposite.setSucc(newOpposite);

        newOpposite.setOpposite(this);
        opposite.setOpposite((WingedEdge) getSucc());
        ((WingedEdge) getSucc()).setOpposite(opposite);
        newOpposite.setCell(opposite.getCell());
        opposite = newOpposite;
    }
}
