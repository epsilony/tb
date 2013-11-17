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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Chain extends RawGeomUnit implements GeomUnit, Iterable<Segment> {

    public static Chain byNodesChain(List<? extends Node> nodesChain) {
        return byNodesChain(nodesChain, true);
    }

    public static Chain byNodesChain(List<? extends Node> nodesChain, boolean closed) {
        if (closed && nodesChain.size() < 3) {
            throw new IllegalArgumentException(String.format(
                    "Each closed chain must contains at least 3 nodes as vertes! "
                            + "this nodes chain has only %d nodes", nodesChain.size()));
        }
        if (!closed && nodesChain.size() < 2) {
            throw new IllegalArgumentException(
                    String.format("Each open chain must contains at least 2 nodes as vertes! "
                            + "this nodes chain has only %d nodes", nodesChain.size()));
        }

        Iterator<? extends Node> nodeIter = nodesChain.iterator();
        Line chainHead = new Line();
        chainHead.setStart(nodeIter.next());
        Line seg = chainHead;
        while (nodeIter.hasNext()) {
            Line succ = new Line(nodeIter.next());
            Segment2DUtils.link(seg, succ);
            seg = succ;
        }

        if (closed) {
            Segment2DUtils.link(seg, chainHead);
        }

        Chain chain = new Chain();
        chain.setHead(chainHead);
        return chain;
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
        for (Segment segment : this) {
            segment.setParent(this);
        }
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
