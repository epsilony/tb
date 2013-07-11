/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.Collection;
import java.util.Iterator;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveUtils {

    public static <ND extends Node> void fission(AdaptiveCell<ND> cell, boolean recursively, Collection<AdaptiveCell<ND>> newCellsOutput) {
        if (recursively) {
            recursivelyFission(cell, newCellsOutput);
        } else if (cell.isAbleToFission()) {
            cell.fission();
            if (null != newCellsOutput) {
                newCellsOutput.clear();
                newCellsOutput.addAll(cell.getChildren());
            }
        }
    }

    public static <ND extends Node> void recursivelyFission(AdaptiveCell<ND> cell, Collection<AdaptiveCell<ND>> newCellsOutput) {
        if (null != newCellsOutput) {
            newCellsOutput.clear();
        }
        _recursivelyFission(cell, newCellsOutput);

    }

    private static <ND extends Node> void _recursivelyFission(AdaptiveCell<ND> cell, Collection<AdaptiveCell<ND>> newCell) {
        do {
            if (cell.isAbleToFission()) {
                cell.fission();
                if (null != newCell) {
                    newCell.addAll(cell.getChildren());
                }
                break;
            } else {
                _recursivelyFission(cell.searchFissionObstrutor(), newCell);
            }
        } while (true);
    }

    public static <ND extends Node> boolean isPointRestrictlyInsideCell(AdaptiveCell<ND> cell, double x, double y) {
        Iterator<AdaptiveCellEdge<ND>> edgeIter = new SegmentIterator<>(cell.getVertexEdge(0));
        while (edgeIter.hasNext()) {
            AdaptiveCellEdge edge = edgeIter.next();
            double[] startCoord = edge.getStart().getCoord();
            double[] endCoord = edge.getEnd().getCoord();
            double cross = Math2D.cross(
                    endCoord[0] - startCoord[0],
                    endCoord[1] - startCoord[1],
                    x - startCoord[0],
                    y - startCoord[1]);
            if (cross <= 0) {
                return false;
            }
        }
        return true;
    }

    public static void linkAsOpposite(AdaptiveCellEdge e1, AdaptiveCellEdge e2) {
        e1.setOpposite(e2);
        e2.setOpposite(e1);
    }

    public static <ND extends Node> void linkEdgeAndCell(AdaptiveCell<ND> cell) {
        for (AdaptiveCellEdge edge : cell) {
            edge.setCell(cell);
        }
    }

    public static <ND extends Node> void linkCornerEdges(AdaptiveCell<ND> cell) {
        for (int i = 0; i < cell.getNumberOfVertes(); i++) {
            Segment2DUtils.link(cell.getVertexEdge(i), cell.getVertexEdge((i + 1) % cell.getNumberOfVertes()));
        }
    }
}
