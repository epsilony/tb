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

package net.epsilony.tb.solid;

import java.util.Iterator;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SegmentStartCoordIterable<T extends Segment> implements Iterable<double[]> {

    T head;

    public SegmentStartCoordIterable(T head) {
        this.head = head;
    }

    @Override
    public Iterator<double[]> iterator() {
        return new Iterator<double[]>() {
            Iterator<T> iter = new SegmentIterator<>(head);

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public double[] next() {
                return iter.next().getStart().getCoord();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
