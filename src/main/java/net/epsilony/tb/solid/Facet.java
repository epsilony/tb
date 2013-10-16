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
public class Facet extends RawGeomUnit implements GeomUnit, Iterable<Segment> {

    public static Facet byCoordChains(double[][][] coordChains, Node nd) {
        ArrayList<ArrayList<Node>> nodeChains = new ArrayList<>(coordChains.length);
        for (double[][] coords : coordChains) {
            ArrayList<Node> nodes = new ArrayList<>(coords.length);
            nodeChains.add(nodes);
            for (double[] coord : coords) {
                if (coords.length < 2) {
                    throw new IllegalArgumentException();
                }
                Node newNode = Node.instanceByClass(nd);
                newNode.setCoord(coord);
                nodes.add(newNode);
            }
        }
        return byNodesChains(nodeChains);
    }

    public static Facet byCoordChains(double[][][] coordChains) {
        return byCoordChains(coordChains, new Node());
    }

    public static Facet byNodesChains(List<? extends List<? extends Node>> nodeChains) {
        if (nodeChains.isEmpty()) {
            throw new IllegalArgumentException("There is at least 1 outer Ring of a Facet");
        }
        Facet facet = new Facet();
        facet.setRings(new ArrayList<Chain>(nodeChains.size()));
        for (List< ? extends Node> nodeChain : nodeChains) {
            Chain ring = Chain.byNodesChain(nodeChain);
            ring.setParent(facet);
            facet.rings.add(ring);
        }
        return facet;
    }

    public static Facet byRingsHeads(List<? extends Segment> heads) {
        Facet facet = new Facet();
        facet.setRings(Chain.byRingsHeads(heads));
        return facet;
    }
    public static final int DIM = 2;
    List<Chain> rings;
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

    public List<Chain> getRings() {
        return rings;
    }

    public void setRings(List<Chain> rings) {
        this.rings = rings;
        for (Chain ring : rings) {
            ring.setParent(this);
        }
    }

    public void fillSegmentsIds() {
        int segId = 0;
        for (Segment seg : this) {
            seg.setId(segId);
            segId++;
        }
    }

    public void fillNodesIds() {
        int ndId = 0;
        for (Segment seg : this) {
            seg.getStart().setId(ndId);
            ndId++;
        }
    }

    public double getMinSegmentCoordLength() {
        double minLen = Double.POSITIVE_INFINITY;
        for (Segment seg : this) {
            double len = Segment2DUtils.chordLength(seg);
            if (len < minLen) {
                minLen = len;
            }
        }
        return minLen;
    }

    public double getMaxSegmentCoordLength() {
        double maxLen = 0;
        for (Segment seg : this) {
            double len = Segment2DUtils.chordLength(seg);
            if (maxLen < len) {
                maxLen = len;
            }
        }
        return maxLen;
    }

    /**
     * Originate from:<\br> Joseph O'Rourke, Computational Geometry in C,2ed.
     * Page 244, Code 7.13
     *
     * @param x
     * @param y
     * @return 'i' : inside , 'o' : outside, 'e' on an edge, 'v' on a vertex
     */
    public char rayCrossing(double x, double y) {
        int rCross = 0, lCross = 0;
        for (Segment seg : this) {
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
        for (Segment seg : this) {
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

    public ArrayList<LinkedList<Node>> getVertes() {
        ArrayList<LinkedList<Node>> res = new ArrayList<>(rings.size());
        for (Chain ring : rings) {
            LinkedList<Node> vs = new LinkedList<>();
            res.add(vs);
            for (Segment seg : ring) {
                vs.add(seg.getStart());
            }
        }
        return res;
    }

    @Override
    public Iterator<Segment> iterator() {
        ArrayList<Segment> chainsHeads = new ArrayList<>(rings.size());
        for (Chain ring : rings) {
            chainsHeads.add(ring.getHead());
        }
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
        for (Chain ring : rings) {
            Segment head = ring.getHead();
            SegmentCoordIterator iter = new SegmentCoordIterator(head);
            if (Math2D.isAnticlockwise(iter)) {
                continue;
            }
            double[] pt = new double[2];

            for (Segment line : new SegmentIterable<>(head)) {
                double[] coord = line.getSucc().getEnd().getCoord();
                if (!Segment2DUtils.isPointStrictlyAtChordRight(line, coord)) {
                    continue;
                }
                Math2D.pointOnSegment(line.getStart().getCoord(), coord, 0.5, pt);
                char rayCrossing = rayCrossing(pt[0], pt[1]);
                boolean added = false;
                int maxCount = 100;
                do {
                    if (rayCrossing == 'e' || rayCrossing == 'v') {
                        break;
                    }
                    if (rayCrossing == 'o') {
                        result.add(pt);
                        added = true;
                        break;
                    }
                    Math2D.pointOnSegment(line.getEnd().getCoord(), pt, 0.1, pt);
                    maxCount--;
                } while (Math2D.distanceSquare(pt, line.getEnd().getCoord()) > 0 && maxCount > 0);
                if (added) {
                    break;
                }
            }
        }
        return result;
    }

    public double calcArea() {
        double area = 0;
        for (Segment seg : this) {
            double[] start = seg.getStart().getCoord();
            double[] end = seg.getEnd().getCoord();
            area += start[0] * end[1] - start[1] * end[0];
        }
        area /= 2;
        return area;
    }

    public Facet fractionize(double lenUpBnd) {
        if (lenUpBnd <= 0) {
            throw new IllegalArgumentException("maxLength should be greater than 0 :" + lenUpBnd);
        }
        Facet res = byNodesChains(getVertes());
        for (Chain ring : res.getRings()) {
            Segment seg = ring.getHead();
            Segment cHead = seg;
            do {
                while (Segment2DUtils.chordLength(seg) > lenUpBnd) {
                    seg.bisect();
                }
                seg = seg.getSucc();
            } while (seg != cHead);
        }
        return res;
    }

    public List<Segment> getRingsHeads() {
        ArrayList<Segment> result = new ArrayList<>(rings.size());
        for (Chain ring : rings) {
            result.add(ring.getHead());
        }
        return result;
    }
}
