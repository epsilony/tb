/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.Iterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SegmentCoordIterator implements Iterator<double[]> {

    SegmentIterator<Segment> segmentIterator;

    public SegmentCoordIterator(Segment head) {
        segmentIterator = new SegmentIterator<>(head);
    }

    @Override
    public boolean hasNext() {
        return segmentIterator.hasNext();
    }

    @Override
    public double[] next() {
        return segmentIterator.next().getStart().getCoord();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
