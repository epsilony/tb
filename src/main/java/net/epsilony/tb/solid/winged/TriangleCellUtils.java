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

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleCellUtils {

    public static void linkOppositesBySameVertes(WingedCell cellA, WingedCell cellB) {
        WingedEdge edgeA = null, edgeB = null;
        boolean finded = false;
        for (int i = 0; i < 3 && !finded; i++) {
            edgeA = cellA.getVertexEdge(i);
            for (int j = 0; j < 3; j++) {
                edgeB = cellB.getVertexEdge(j);
                if (edgeA.getStart() == edgeB.getEnd() && edgeA.getEnd() == edgeB.getStart()) {
                    finded = true;
                    break;
                }
            }
        }
        if (!finded) {
            throw new IllegalArgumentException();
        }
        WingedUtils.linkAsOpposite(edgeA, edgeB);
    }
}
