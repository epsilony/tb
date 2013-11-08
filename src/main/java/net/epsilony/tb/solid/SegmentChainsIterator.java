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
public class SegmentChainsIterator<T extends Segment> implements Iterator<T> {

    Iterator<? extends T> startIterator;
    T seg;
    T last;
    T start;

    public SegmentChainsIterator(Iterable<? extends T> chainsHeads) {
        startIterator = chainsHeads.iterator();
        seg = startIterator.hasNext() ? startIterator.next() : null;
        start = seg;
    }

    @Override
    public boolean hasNext() {
        return seg != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        T res = seg;
        seg = (T) seg.getSucc();
        if (seg.getPred() != res || seg.getPred() == seg) {
            throw new IllegalStateException("Meet broken Segment2D link, may cause self ring");
        }
        if (seg == start) {
            if (startIterator.hasNext()) {
                seg = startIterator.next();
            } else {
                seg = null;
            }
            start = seg;
        }
        last = res;
        return res;
    }

    @Override
    public void remove() {
        if (last.getPred().getPred() == last.getSucc()) {
            throw new IllegalStateException("The chain is only a triangle, and no segments can be removed!");
        }
        Segment2DUtils.link(last.getPred(), last.getSucc());
    }
}
