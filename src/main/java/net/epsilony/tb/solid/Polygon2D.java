/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Polygon2D implements Iterable<Line2D> {

    public static final int DIM = 2;
    ArrayList<Line2D> chainsHeads;

    public static Polygon2D byCoordChains(double[][][] coordChains) {
        ArrayList<ArrayList<Node>> nodeChains = new ArrayList<>(coordChains.length);
        for (double[][] coords : coordChains) {
            ArrayList<Node> nodes = new ArrayList<>(coords.length);
            nodeChains.add(nodes);
            for (double[] coord : coords) {
                nodes.add(new Node(coord));
            }
        }
        return new Polygon2D(nodeChains);
    }
    private DifferentiableFunction levelSetFunction = new DifferentiableFunction() {
        @Override
        public int getInputDimension() {
            return 2;
        }

        @Override
        public int getOutputDimension() {
            return 1;
        }

        @Override
        public double[] value(double[] input, double[] output) {
            if (null == output) {
                output = new double[1];
            }
            output[0] = -distanceFunc(input[0], input[1]);
            return output;
        }

        @Override
        public int getDiffOrder() {
            return 0;
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            if (diffOrder != 0) {
                throw new IllegalArgumentException();
            }
        }
    };

    public Polygon2D(List<? extends List<? extends Node>> nodeChains) {
        if (nodeChains.isEmpty()) {
            throw new IllegalArgumentException("There is at least 1 chain in a Polygon");
        }
        chainsHeads = new ArrayList<>(nodeChains.size());
        for (List< ? extends Node> nds : nodeChains) {
            if (nds.size() < 3) {
                throw new IllegalArgumentException(
                        String.format(
                        "Each chain in a polygon must contain at least 3 nodes as vertes! "
                        + "nodesChain[%d] has only %d nodes",
                        nodeChains.indexOf(nds), nds.size()));
            }
            Line2D chainHead = new Line2D();
            Line2D seg = chainHead;
            for (Node nd : nds) {
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

        refresh();
    }

    public Polygon2D() {
    }

    public void setChainsHeads(List<? extends Line2D> chainsHeads) {
        this.chainsHeads = new ArrayList<>(chainsHeads);
        refresh();
    }

    private void refresh() {
        fillSegmentsIds();
    }

    private void fillSegmentsIds() {
        int id = 0;
        for (Line2D seg : this) {
            seg.setId(id);
            id++;
        }
    }

    public ArrayList<Line2D> getChainsHeads() {
        return chainsHeads;
    }

    public double getMinSegmentLength() {
        double minLen = Double.POSITIVE_INFINITY;
        for (Line2D seg : this) {
            double len = seg.length();
            if (len < minLen) {
                minLen = len;
            }
        }
        return minLen;
    }

    public double getMaxSegmentLength() {
        double maxLen = 0;
        for (Line2D seg : this) {
            double len = seg.length();
            if (maxLen < len) {
                maxLen = len;
            }
        }
        return maxLen;
    }

    /**
     * Originate from:<\br> Joseph O'Rourke, Computational Geometry in C,2ed. Page 244, Code 7.13
     *
     * @param x
     * @param y
     * @return 'i' : inside , 'o' : outside, 'e' on an edge, 'v' on a vertex
     */
    public char rayCrossing(double x, double y) {
        int rCross = 0, lCross = 0;
        for (Line2D seg : this) {
            Node start = seg.getStart();
            double x1 = start.coord[0];
            double y1 = start.coord[1];
            if (x1 == x && y1 == y) {
                return 'v';
            }
            Node end = seg.getEnd();
            double x2 = end.coord[0];
            double y2 = end.coord[1];

            boolean rStrad = (y1 > y) != (y2 > y);
            boolean lStrad = (y1 < y) != (y2 < y);

            if (rStrad || lStrad) {
                if (rStrad && x1 > x && x2 > x) {
                    rCross += 1;
                } else if (lStrad && x1 < x && x2 < x) {
                    lCross += 1;
                } else {
                    double xCross = (x1 * y - x1 * y2 - x2 * y + x2 * y1) / (y1 - y2);
                    if (rStrad && xCross > x) {
                        rCross++;
                    }
                    if (lStrad && xCross < x) {
                        lCross++;
                    }
                }
            }
        }
        rCross %= 2;
        lCross %= 2;
        if (rCross != lCross) {
            return 'e';
        }
        if (rCross == 1) {
            return 'i';
        } else {
            return 'o';
        }
    }

    public double distanceFunc(double x, double y) {
        char rayCrs = rayCrossing(x, y);

        if (rayCrs == 'e' || rayCrs == 'v') {
            return 0;
        }

        double inf = Double.POSITIVE_INFINITY;
        for (Line2D seg : this) {
            double dst = Segment2DUtils.distanceToChord(seg, x, y);
            if (dst < inf) {
                inf = dst;
            }
        }
        return rayCrs == 'i' ? inf : -inf;
    }

    public DifferentiableFunction getLevelSetFunction() {
        return levelSetFunction;
    }

    public Polygon2D fractionize(double lenUpBnd) {
        if (lenUpBnd <= 0) {
            throw new IllegalArgumentException("maxLength should be greater than 0 :" + lenUpBnd);
        }
        Polygon2D res = new Polygon2D(getVertes());
        for (Line2D cHead : res.chainsHeads) {
            Line2D seg = cHead;
            do {
                while (seg.length() > lenUpBnd) {
                    seg.bisect();
                }
                seg = (Line2D) seg.succ;
            } while (seg != cHead);
        }
        res.refresh();
        return res;
    }

    public ArrayList<LinkedList<Node>> getVertes() {
        ArrayList<LinkedList<Node>> res = new ArrayList<>(chainsHeads.size());
        for (Line2D cHead : chainsHeads) {
            LinkedList<Node> vs = new LinkedList<>();
            res.add(vs);
            Line2D seg = cHead;
            do {
                vs.add(seg.getStart());
                seg = (Line2D) seg.succ;
            } while (seg != cHead);
        }
        return res;
    }

    @Override
    public Iterator<Line2D> iterator() {
        return new SegmentChainsIterator<>(chainsHeads);
    }

    public List<Segment> getSegments() {
        LinkedList<Segment> segments = new LinkedList<>();
        for (Segment seg : this) {
            segments.add(seg);
        }
        return segments;
    }

    public List<double[]> getPointsInHoles() {
        List<double[]> result = new LinkedList<>();
        for (Line2D head : chainsHeads) {
            SegmentCoordIterator iter = new SegmentCoordIterator(head);
            if (!Math2D.isAnticlockwise(iter)) {
                continue;
            }
            double[] pt = new double[2];
            int maxCount = 100;
            for (Line2D line : new SegmentIterable<>(head)) {
                double[] coord = line.getSucc().getEnd().getCoord();
                if (Segment2DUtils.isPointStrictlyAtChordLeft(line, coord)) {
                    Math2D.pointOnSegment(line.getStartCoord(), coord, 0.5, pt);
                }
                char rayCrossing = rayCrossing(pt[0], pt[1]);
                boolean added = false;
                do {
                    if (rayCrossing == 'i') {
                        result.add(pt);
                        added = true;
                        break;
                    }
                    Math2D.pointOnSegment(line.getEndCoord(), pt, 0.1, pt);
                    maxCount--;
                } while (Math2D.distanceSquare(pt, line.getEndCoord()) > 0 && maxCount > 0);
                if (added) {
                    break;
                }
            }
        }
        return result;
    }
}
