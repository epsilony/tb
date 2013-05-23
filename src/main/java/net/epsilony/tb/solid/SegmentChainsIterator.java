/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SegmentChainsIterator<T extends Segment> implements Iterator<T> {

    Iterator<T> startIterator;
    T seg;
    T last;
    T start;

    public SegmentChainsIterator(List<T> chainsHeads) {
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
