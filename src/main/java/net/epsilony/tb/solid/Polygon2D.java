/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Polygon2D<ND extends Node> extends GeneralPolygon2D<Line2D<ND>, ND> {

    public static <ND extends Node> Polygon2D<ND> byCoordChains(double[][][] coordChains, ND nd) {
        ArrayList<ArrayList<ND>> nodeChains = new ArrayList<>(coordChains.length);
        for (double[][] coords : coordChains) {
            ArrayList<ND> nodes = new ArrayList<>(coords.length);
            nodeChains.add(nodes);
            for (double[] coord : coords) {
                if (coords.length < 2) {
                    throw new IllegalArgumentException();
                }
                ND newNode = Node.instanceByClass(nd);
                newNode.setCoord(coord);
                nodes.add(newNode);
            }
        }
        return new Polygon2D<>(nodeChains);
    }

    public static Polygon2D<Node> byCoordChains(double[][][] coordChains) {
        return byCoordChains(coordChains, new Node());
    }

    public Polygon2D(List<? extends List<? extends ND>> nodeChains) {
        if (nodeChains.isEmpty()) {
            throw new IllegalArgumentException("There is at least 1 chain in a Polygon");
        }
        chainsHeads = new ArrayList<>(nodeChains.size());
        for (List< ? extends ND> nds : nodeChains) {
            if (nds.size() < 3) {
                throw new IllegalArgumentException(
                        String.format(
                        "Each chain in a polygon must contain at least 3 nodes as vertes! "
                        + "nodesChain[%d] has only %d nodes",
                        nodeChains.indexOf(nds), nds.size()));
            }
            Line2D<ND> chainHead = new Line2D();
            Line2D<ND> seg = chainHead;
            for (ND nd : nds) {
                seg.start = nd;
                Line2D succ = new Line2D();
                seg.succ = succ;
                succ.pred = seg;
                seg = succ;
            }
            chainHead.pred = seg.pred;
            chainHead.pred.setSucc(chainHead);
            chainsHeads.add(chainHead);
        }
    }

    public Polygon2D() {
    }

//    public Polygon2D(GeneralPolygon2D<? extends Segment, ? extends Node> plg) {
//        ArrayList<? extends Segment<?, ? extends Node>> plgHeads = plg.getChainsHeads();
//        if (null == plgHeads) {
//            return;
//        }
//        chainsHeads = new ArrayList<>(plgHeads.size());
//        for (Segment head : plgHeads) {
//            Node node = new Node(head.getStart().getCoord());
//            Line2D newHead = new Line2D(node);
//            chainsHeads.add(newHead);
//            Iterator<Segment> iter = new SegmentIterator<>(head);
//            iter.next();
//            Line2D pred = newHead;
//            while (iter.hasNext()) {
//                Segment next = iter.next();
//                Line2D newNext = new Line2D(new Node(next.getStart().getCoord()));
//                Segment2DUtils.link(pred, newNext);
//                pred = newNext;
//            }
//            Segment2DUtils.link(pred, newHead);
//        }
//    }

    public double getMinSegmentLength() {
        return getMinSegmentCoordLength();
    }

    public double getMaxSegmentLength() {
        return getMaxSegmentCoordLength();
    }

    public Polygon2D<ND> fractionize(double lenUpBnd) {
        if (lenUpBnd <= 0) {
            throw new IllegalArgumentException("maxLength should be greater than 0 :" + lenUpBnd);
        }
        Polygon2D<ND> res = new Polygon2D<>(getVertes());
        for (Line2D<ND> cHead : res.chainsHeads) {
            Line2D<ND> seg = cHead;
            do {
                while (seg.length() > lenUpBnd) {
                    seg.bisect();
                }
                seg = (Line2D) seg.succ;
            } while (seg != cHead);
        }
        return res;
    }

    public double calcArea() {
        double area = 0;
        for (Segment seg : this) {
            double[] start = seg.getStart().getCoord();
            double[] end = seg.getEnd().getCoord();
            double dx = end[0] - start[0];
            double dy = end[1] - start[1];
            area += start[0] * dy - start[1] * dx;
        }
        area /= 2;
        return area;
    }
}
