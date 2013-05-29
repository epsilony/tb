/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.Iterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SegmentIterable<T extends Segment> implements Iterable<T> {

    T head;

    public SegmentIterable(T head) {
        this.head = head;
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<>(head);
    }
}
