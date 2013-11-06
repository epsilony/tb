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

package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ContourUtils {

    public static double getMinSegmentLength(List<Line> contourHeads) {
        double min = Double.POSITIVE_INFINITY;
        for (Line head : contourHeads) {
            SegmentIterator<Line> iter = new SegmentIterator<>(head);
            while (iter.hasNext()) {
                Line seg = iter.next();
                double segLen = Segment2DUtils.chordLength(seg);
                if (segLen < min) {
                    min = segLen;
                }
            }
        }
        return min;
    }
}
