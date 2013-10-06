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

    @Override
    public Iterator<Segment> iterator() {
        return new SegmentIterator<>(head);
    }
}
