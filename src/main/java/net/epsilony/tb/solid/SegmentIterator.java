/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.Iterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SegmentIterator<T extends Segment> implements Iterator<T> {

    T next;
    T start;
    //T last;

    public SegmentIterator(T start) {
        this.start = start;
        next = start;
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public T next() {
        T last = next;
        next = (T) next.getSucc();
        if (next == start) {
            next = null;
        }
        if (next != null) {
            if (next.getPred() != last) {
                throw new IllegalStateException("chain broken!");
            }
        }
        return last;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
