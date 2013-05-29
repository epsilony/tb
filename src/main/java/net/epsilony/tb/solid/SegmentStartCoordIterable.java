/* (c) Copyright by Man YUAN */
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
