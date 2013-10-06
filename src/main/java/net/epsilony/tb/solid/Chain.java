/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Chain extends RawGeomUnit implements GeomUnit, Iterable<Segment> {

    public static Chain byNodesChain(List<? extends Node> nodesChain) {
        if (nodesChain.size() < 3) {
            throw new IllegalArgumentException(
                    String.format(
                    "Each chain in a polygon must contain at least 3 nodes as vertes! "
                    + "this nodes chain has only %d nodes",
                    nodesChain.size()));
        }
        Line chainHead = new Line();
        Line seg = chainHead;
        for (Node nd : nodesChain) {
            seg.start = nd;
            Line succ = new Line();
            seg.succ = succ;
            succ.pred = seg;
            seg = succ;
        }
        chainHead.pred = seg.pred;
        chainHead.pred.setSucc(chainHead);

        Chain ring = new Chain();
        ring.setHead(chainHead);
        return ring;
    }

    public static ArrayList<Chain> byRingsHeads(List<? extends Segment> heads) {
        ArrayList<Chain> rings = new ArrayList<>(heads.size());
        for (Segment head : heads) {
            Chain ring = new Chain();
            ring.setHead(head);
            rings.add(ring);
        }
        return rings;
    }

    public Chain() {
    }

    public Chain(Segment head) {
        this.head = head;
    }
    Segment head;

    public void setParent(Facet parent) {
        this.parent = parent;
    }

    public Segment getHead() {
        return head;
    }

    public void setHead(Segment head) {
        this.head = head;
        for (Segment seg : this) {
            seg.setParent(this);
        }
    }

    public boolean isClosed() {
        if (head.getPred() == null) {
            return false;
        }
        Segment last = getLast();
        if (head.getPred() == last) {
            return true;
        }
        return false;
    }

    public void checkAsRing() {
        if (head.getPred() == null) {
            throw new IllegalStateException();
        }
        Segment last = getLast();
        if (head.getPred() != last) {
            throw new IllegalArgumentException();
        }
    }

    public void checkChainsHead() {
        if (head.getPred() != null && getLast() != head.getPred()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public Iterator<Segment> iterator() {
        return new SegmentIterator<>(head);
    }

    public Segment getLast() {
        Segment last = null;
        for (Segment seg : this) {
            last = seg;
        }
        return last;
    }
}
