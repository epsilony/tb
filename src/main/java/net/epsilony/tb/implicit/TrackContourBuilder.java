/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;
import net.epsilony.tb.solid.winged.WingedUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TrackContourBuilder extends AbstractTriangleContourBuilder {

    TrackContourSpecification specification = new TrackContourSpecification();
    protected ImplicitFunctionSolver implicitFunctionSolver = null;
    OnLineFunction onLineFunction = new OnLineFunction();
    SimpleBisectionSolver onLineSolver = new SimpleBisectionSolver();

    @Override
    public void genContour() {
        prepareToGenContour();
        boolean shouldHaveContour = false;

        TriangleContourCell headCell = nextUnvisitedCellWithContour();
        if (null != headCell) {
            shouldHaveContour = true;
        }

        while (headCell != null) {
            ContourNode headNode = genHeadNode(headCell);
            if (null != headNode) {
                trackContour(headNode, headCell);
            } else {
                headCell.setVisited(true);
            }
            headCell = nextUnvisitedCellWithContour();
        }
        if (null == getContourHeads() && shouldHaveContour) {
            throw new IllegalStateException("should have contour but not!");
        }
    }

    @Override
    public void prepareToGenContour() {
        super.prepareToGenContour();
        openRingsHeads.clear();
        for (TriangleContourCell cell : cells) {
            cell.getPassByContourLines(null);
        }
    }

    private ContourNode genHeadNode(TriangleContourCell headCell) {
        double[][] roughHeadInfo = estimateHeadInfo(headCell);
        double[] roughHeadCoord = roughHeadInfo[0];
        double[] roughUnitContourDirection = roughHeadInfo[1];
        boolean statusGood = implicitFunctionSolver.solve(roughHeadCoord);
        if (!statusGood) {
            throw new IllegalStateException("solver failed");
        }
        ContourNode head = new ContourNode();
        head.setCoord(implicitFunctionSolver.getSolution());
        head.setFunctionValue(implicitFunctionSolver.getFunctionValue());
        if (isHeadPointEligible(head, roughUnitContourDirection, headCell)) {
            return head;
        }
        return null;
    }

    public static double[][] estimateHeadInfo(TriangleContourCell startCell) {
        TriangleContourCellEdge contourSourceEdge = startCell.getContourSourceEdge();
        TriangleContourCellEdge contourDestinationEdge = startCell.getContourDestinationEdge();
        double[] sourceEdgePoint = genLinearInterpolateContourPoint(contourSourceEdge);
        double[] destEdgePoint = genLinearInterpolateContourPoint(contourDestinationEdge);

        double[] roughPoint = Math2D.pointOnSegment(sourceEdgePoint, destEdgePoint, 0.5, destEdgePoint);
        double[] vec = Math2D.subs(destEdgePoint, sourceEdgePoint, sourceEdgePoint);
        double[] roughUnitContourDirection = Math2D.normalize(vec, vec);
        return new double[][]{roughPoint, roughUnitContourDirection};

    }

    public boolean isHeadPointEligible(ContourNode head, double[] roughNextDirection, TriangleContourCell headCell) {
        if (!specification.isHeadPointNormalEligible(head, roughNextDirection)) {
            return false;
        }
        double[] headCoord = head.getCoord();
        for (int i = 0; i < headCell.getNumberOfVertes(); i++) {
            TriangleContourCellEdge edge = headCell.getVertexEdge(i);
            if (!Segment2DUtils.isPointStrictlyAtChordLeft(edge, headCoord)) {
                return false;
            }
        }

        Set<TriangleContourCell> nodesNeighbours = WingedUtils.getNodesNeighbours(headCell);

        for (TriangleContourCell neighbour : nodesNeighbours) {
            List<Line> passBySegs = neighbour.getPassByContourLines();
            if (null == passBySegs) {
                continue;
            }
            for (Line neighbourSeg : passBySegs) {
                ContourNode neiStart = (ContourNode) neighbourSeg.getStart();
                ContourNode neiEnd = (ContourNode) neighbourSeg.getEnd();
                if (specification.isSegmentEligible(neiStart, head) || specification.isSegmentEligible(head, neiEnd)) {
                    return false;
                }
            }
        }

        return true;
    }
    private static final double ROUGH_DISTANCE_SHRINK = 0.8;
    private static final double SEARCH_DISTANCE_ENLARGE = 1.6;

    public void trackContour(ContourNode headNode, TriangleContourCell headCell) {
        ContourNode headSucc = genHeadSuccNode(headNode);
        if (null == headSucc) {
            return;
        }
        Line head = new Line();
        head.setStart(headNode);
        Line seg = new Line(headSucc);
        Segment2DUtils.link(head, seg);
        openRingsHeads.add(head);
        contourHeads.add(head);

        TriangleContourCell cell = headCell;
        cell.setVisited(true);
        while (true) {
            cell = markVisitiedAndReturnLast(cell, (Line) seg.getPred());
            if (cell == null) {
                break;
            }
            Line nextNew = genSuccSeg(seg);
            if (nextNew == null) {
                markVisitiedAndReturnLast(cell, (Line) seg);
                if (seg.getSucc() != head) {
                    contourHeads.remove((Line) seg.getSucc());
                }
                openRingsHeads.remove((Line) seg.getSucc());
                //TODO here: divide seg.getSucc() if seg.length *1.5<seg.getSucc().length
                break;
            }
            seg = nextNew;
        }

    }

    private ContourNode genHeadSuccNode(ContourNode headNode) {
        ContourNode headSucc = trackNextNode(
                (specification.getMaxSegmentLength() + specification.getMinSegmentLength()) / 2,
                headNode);
        return headSucc;
    }

    public Line genSuccSeg(Line segment) {
        ContourNode ndA = (ContourNode) segment.getPred().getStart();
        ContourNode ndB = (ContourNode) segment.getStart();
        double distance = specification.genNextRoughPointDistance(ndA, ndB);
        double headSearchRadius = distance * SEARCH_DISTANCE_ENLARGE;
        while (distance >= specification.getMinSegmentLength()) {
            Line otherHead = searchOtherHeadAsSuccCandidate(headSearchRadius, ndB);
            if (null == otherHead) {
                break;
            } else {
                if (specification.isSegmentEligible(ndB, (ContourNode) otherHead.getStart())) {
                    Segment2DUtils.link(segment, otherHead);
                    return null;
                } else {
                    distance = 0.5 * Math2D.distance(ndB.getCoord(), otherHead.getStartCoord());
                }
            }
        }

        ContourNode next = trackNextNode(distance, ndB);

        if (null == next) {
            next = trackNextNodeHarder(ndB);
        }
        if (null == next) {
            throw new IllegalStateException();
        }
        Line result = new Line(next);

        Segment2DUtils.link(segment, result);
        return result;
    }

    private ContourNode trackNextNode(double roughDistance, ContourNode preNode) {
        ContourNode next = new ContourNode();
        while (roughDistance >= specification.getMinSegmentLength()) {
            double[] rough = genRoughPointByGradient(preNode, roughDistance);

            if (!implicitFunctionSolver.solve(rough)) {
                roughDistance *= ROUGH_DISTANCE_SHRINK;
                continue;
            }
            next.setCoord(implicitFunctionSolver.getSolution());
            next.setFunctionValue(implicitFunctionSolver.getFunctionValue());
            if (Math.abs(next.getFunctionValue()[0]) <= 1e-3
                    && specification.isSegmentEligible(preNode, next)) {
                return next;
            }
            roughDistance *= ROUGH_DISTANCE_SHRINK;
        }
        return null;
    }

    private ContourNode trackNextNodeHarder(final ContourNode node) {
        double distance = specification.getMinSegmentLength();
        double searchDistance = distance * SEARCH_DISTANCE_ENLARGE;
        double[] nodeCoord = node.getCoord();
        List<Line> lines = new LinkedList<>();
        for (Line head : contourHeads) {
            SegmentIterator<Line> segIter = new SegmentIterator<>(head);
            while (segIter.hasNext()) {
                Line seg = segIter.next();
                if (seg.getSucc() == null) {
                    break;
                }
                if (Segment2DUtils.distanceToChord(seg, nodeCoord) < searchDistance) {
                    lines.add(seg);
                }
            }
        }
        int stepNum = 360;
        double stepAngle = Math.PI * 2 / stepNum;
        double[] curveStart = new double[2];
        double[] curveEnd = new double[2];
        curveStart[0] = nodeCoord[0] + distance;
        curveStart[1] = nodeCoord[1];
        double[] lsv = new double[1];
        List<ContourNode> points = new LinkedList<>();
        for (int i = 0; i < stepNum; i++) {
            double endAngle = i + 1 < stepNum ? (i + 1) * stepAngle : 0;
            double endCos = Math.cos(endAngle);
            double endSin = Math.sin(endAngle);
            curveEnd[0] = nodeCoord[0] + endCos * distance;
            curveEnd[1] = nodeCoord[1] + endSin * distance;
            levelSetFunction.setDiffOrder(0);
            levelSetFunction.value(curveStart, lsv);
            double startV = lsv[0];
            levelSetFunction.value(curveEnd, lsv);
            double endV = lsv[0];
            if (endV * startV <= 0) {
                ContourNode point = searchPointOnSegment(curveStart, curveEnd);
                points.add(point);
            }
            curveStart[0] = curveEnd[0];
            curveStart[1] = curveEnd[1];
        }

        if (points.isEmpty()) {
            throw new IllegalStateException();
        }

        for (Line line : lines) {
            Iterator<ContourNode> pointIter = points.iterator();
            double[] endCoord = line.getEndCoord();
            double[] startCoord = line.getStartCoord();
            while (pointIter.hasNext()) {
                ContourNode point = pointIter.next();
                if (specification.isSegmentEligible((ContourNode) line.getStart(), point)) {

                    double[] pointCoord = point.getCoord();
                    double ds = Math2D.distance(startCoord, pointCoord);
                    double de = Math2D.distance(endCoord, pointCoord);
                    double l = Segment2DUtils.chordLength(line);
                    double d = Math2D.cross(
                            endCoord[0] - startCoord[0], endCoord[1] - startCoord[1],
                            pointCoord[0] - startCoord[0], pointCoord[1] - startCoord[1]) / l;
                    double t = l * l + d * d;
                    if (t > ds * ds || t > de * de) {
                        pointIter.remove();
                    }
                }
            }
        }

        if (points.isEmpty()) {
            return null;
        }

        if (points.size() == 1) {
            return points.get(0);
        }
        Iterator<ContourNode> pointsIter = points.iterator();
        ContourNode bestResult = pointsIter.next();
        double bestCos = ContourNode.gradientCos(node, bestResult);
        do {
            ContourNode nd = pointsIter.next();
            double cosValue = ContourNode.gradientCos(node, nd);
            if (cosValue > bestCos) {
                bestCos = cosValue;
                bestResult = nd;
            }
        } while (pointsIter.hasNext());
        return bestResult;
    }

    public ContourNode searchPointOnSegment(double[] start, double[] end) {
        onLineFunction.prepareToSolve(start, end);
        onLineSolver.solve(new double[]{0.5});
        double t = onLineSolver.getSolution()[0];
        double[] coord = Math2D.pointOnSegment(start, end, t, null);
        levelSetFunction.setDiffOrder(1);
        ContourNode result = new ContourNode();
        result.setCoord(coord);
        result.setFunctionValue(levelSetFunction.value(coord, null));
        return result;
    }

    private Line searchOtherHeadAsSuccCandidate(double distance, ContourNode node) {
        Line result = null;
        double[] nodeCoord = node.getCoord();
        double[] normal = Arrays.copyOfRange(node.getFunctionValue(), 1, 3);
        Math2D.normalize(normal, normal);
        for (Line head : openRingsHeads) {
            ContourNode headNode = (ContourNode) head.getStart();
            double[] headCoord = headNode.getCoord();
            if (Math2D.distance(headCoord, nodeCoord) > distance) {
                continue;
            }

            if (specification.isOtherHeadInCandidateDirection(headNode, nodeCoord, normal)) {
                return head;
            }
        }
        return result;
    }

    public static TriangleContourCell markVisitiedAndReturnLast(TriangleContourCell cell, Line seg) {

        TriangleContourCellEdge sourceEdge = null;
        do {
            cell.setVisited(true);
            List<Line> passBySegs = cell.getPassByContourLines();
            if (null == passBySegs) {
                passBySegs = new LinkedList<>();
                cell.getPassByContourLines(passBySegs);
            }
            passBySegs.add(seg);
            TriangleContourCellEdge destEdge = getCellEdgeIntersectingSegment(cell, sourceEdge, seg);
            if (null == destEdge) {
                break;
            }
            sourceEdge = destEdge.getOpposite();
            if (null == sourceEdge) {
                cell = null;
                break;
            }
            cell = (TriangleContourCell) sourceEdge.getCell();
        } while (true);
        return cell;
    }

    public static TriangleContourCellEdge getCellEdgeIntersectingSegment(
            TriangleContourCell cell,
            TriangleContourCellEdge except,
            Line seg) {
        TriangleContourCellEdge result = null;
        for (int i = 0; i < cell.getNumberOfVertes(); i++) {
            TriangleContourCellEdge edge = cell.getVertexEdge(i);
            if (edge == except) {
                continue;
            }
            if (Math2D.isSegmentsIntersecting(
                    edge.getStartCoord(), edge.getEndCoord(),
                    seg.getStartCoord(), seg.getEndCoord())) {
                result = edge;
                break;
            }
        }
        return result;
    }

    private double[] genRoughPointByGradient(ContourNode node, double distance) {
        double[] functionValue = node.getFunctionValue();
        double gradX = functionValue[1];
        double gradY = functionValue[2];

        double[] result = new double[]{-gradY, gradX};
        Math2D.normalize(result, result);
        Math2D.scale(result, distance, result);
        double[] coord = node.getCoord();
        result[0] += coord[0];
        result[1] += coord[1];
        return result;
    }

    public TrackContourSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TrackContourSpecification specification) {
        this.specification = specification;
        if (null == specification) {
            throw new IllegalArgumentException("specification should not be null");
        }
    }

    public ImplicitFunctionSolver getImplicitFunctionSolver() {
        return implicitFunctionSolver;
    }

    public void setImplicitFunctionSolver(ImplicitFunctionSolver implicitFunctionSolver) {
        this.implicitFunctionSolver = implicitFunctionSolver;
        if (null != implicitFunctionSolver) {
            implicitFunctionSolver.setFunction(levelSetFunction);
            onLineSolver.setFunctionAbsoluteTolerence(implicitFunctionSolver.getFunctionAbsoluteTolerence());
            onLineSolver.setFunction(onLineFunction);
            onLineSolver.setLowerBounds(new double[]{0});
            onLineSolver.setUpperBounds(new double[]{1});
            onLineSolver.setMaxEval(200);
        }
    }
}
