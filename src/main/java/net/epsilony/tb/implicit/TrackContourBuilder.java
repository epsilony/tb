/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.epsilony.tb.adaptive.AdaptiveCellEdge;
import net.epsilony.tb.adaptive.TriangleAdaptiveCell;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TrackContourBuilder extends AbstractTriangleContourBuilder {

    TrackContourSpecification specification = new TrackContourSpecification();

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
        boolean statusGood = newtonSolver.solve(roughHeadCoord);
        if (!statusGood) {
            throw new IllegalStateException("Newton's solver failed");
        }
        ContourNode head = new ContourNode();
        head.setCoord(newtonSolver.getSolution());
        head.setFunctionValue(newtonSolver.getFunctionValue());
        if (isHeadPointEligible(head, roughUnitContourDirection, headCell)) {
            return head;
        }
        return null;
    }

    public static double[][] estimateHeadInfo(TriangleContourCell startCell) {
        Line2D contourSourceEdge = startCell.getContourSourceEdge();
        Line2D contourDestinationEdge = startCell.getContourDestinationEdge();
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
        for (AdaptiveCellEdge edge : headCell) {
            if (!Segment2DUtils.isPointStrictlyAtChordLeft(edge, headCoord)) {
                return false;
            }
        }

        Set<TriangleAdaptiveCell> nodesNeighbours = headCell.getNodesNeighbours();

        for (TriangleAdaptiveCell neighbour : nodesNeighbours) {
            List<Line2D> passBySegs = ((TriangleContourCell) neighbour).getPassByContourLines();
            if (null == passBySegs) {
                continue;
            }
            for (Line2D neighbourSeg : passBySegs) {
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
        ContourNode headSucc = trackNextNode(specification.getExpectSegmentLength(), headNode, true);
        if (null == headSucc) {
            return;
        }
        Line2D head = new Line2D();
        head.setStart(headNode);
        Line2D seg = new Line2D(headSucc);
        Segment2DUtils.link(head, seg);
        openRingsHeads.add(head);
        contourHeads.add(head);

        TriangleContourCell cell = headCell;
        cell.setVisited(true);
        while (true) {
            cell = markVisitiedAndReturnLast(cell, (Line2D) seg.getPred());
            if (cell == null) {
                break;
            }
            Line2D nextNew = nextNewSucc(seg);
            if (nextNew == null) {
                markVisitiedAndReturnLast(cell, (Line2D) seg);
                if (seg.getSucc() != head) {
                    contourHeads.remove((Line2D) seg.getSucc());
                }
                openRingsHeads.remove((Line2D) seg.getSucc());
                break;
            }
            seg = nextNew;
        }

    }

    public Line2D nextNewSucc(Line2D segment) {
        ContourNode ndA = (ContourNode) segment.getPred().getStart();
        ContourNode ndB = (ContourNode) segment.getStart();
        double distance = specification.genNextRoughPointDistance(ndA, ndB);
        double headSearchRadius = distance * SEARCH_DISTANCE_ENLARGE;
        Line2D otherHead = searchOtherHeadAsSuccCandidate(headSearchRadius, ndB);
        if (null != otherHead) {
            if (specification.isSegmentEligible(ndB, (ContourNode) otherHead.getStart())) {
                Segment2DUtils.link(segment, otherHead);
                return null;
            } else {
                distance = 0.5 * Math2D.distance(ndB.getCoord(), otherHead.getStartCoord());
            }
        }
        ContourNode next = trackNextNode(distance, ndB, true);
        if (null == next) {
            throw new IllegalStateException();
        }
        Line2D result = new Line2D(next);

        Segment2DUtils.link(segment, result);
        return result;
    }

    private ContourNode trackNextNode(double roughDistance, ContourNode preNode, boolean tryHard) {
        ContourNode next = new ContourNode();
        while (tryHard && roughDistance >= specification.getMinSegmentLength()) {
            double[] rough = genRoughPointByGradient(preNode, roughDistance);

            if (!newtonSolver.solve(rough)) {
                roughDistance *= ROUGH_DISTANCE_SHRINK;
                continue;
            }
            next.setCoord(newtonSolver.getSolution());
            next.setFunctionValue(newtonSolver.getFunctionValue());
            if (specification.isSegmentEligible(preNode, next)) {
                return next;
            }
            roughDistance *= ROUGH_DISTANCE_SHRINK;
        }
        return null;
    }

    private Line2D searchOtherHeadAsSuccCandidate(double distance, ContourNode node) {
        Line2D result = null;
        double[] nodeCoord = node.getCoord();
        double[] normal = Arrays.copyOfRange(node.getFunctionValue(), 1, 3);
        Math2D.normalize(normal, normal);
        for (Line2D head : openRingsHeads) {
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

    public static TriangleContourCell markVisitiedAndReturnLast(TriangleContourCell cell, Line2D seg) {

        AdaptiveCellEdge sourceEdge = null;
        do {
            cell.setVisited(true);
            List<Line2D> passBySegs = cell.getPassByContourLines();
            if (null == passBySegs) {
                passBySegs = new LinkedList<>();
                cell.getPassByContourLines(passBySegs);
            }
            passBySegs.add(seg);
            AdaptiveCellEdge destEdge = getCellEdgeIntersectingSegment(cell, sourceEdge, seg);
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

    public static AdaptiveCellEdge getCellEdgeIntersectingSegment(
            TriangleContourCell cell,
            AdaptiveCellEdge except,
            Line2D seg) {
        AdaptiveCellEdge result = null;
        for (AdaptiveCellEdge edge : cell) {
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
}
