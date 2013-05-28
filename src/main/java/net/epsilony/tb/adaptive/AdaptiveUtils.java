/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.epsilony.tb.Math2D;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveUtils {

    public static void fission(AdaptiveCell cell, boolean recursively, Collection<AdaptiveCell> newCellsOutput) {
        if (recursively) {
            recursivelyFission(cell, newCellsOutput);
        } else if (cell.isAbleToFission()) {
            cell.fission();
            if (null != newCellsOutput) {
                newCellsOutput.clear();
                newCellsOutput.addAll(Arrays.asList(cell.getChildren()));
            }
        }
    }

    public static void recursivelyFission(AdaptiveCell cell, Collection<AdaptiveCell> newCellsOutput) {
        if (null != newCellsOutput) {
            newCellsOutput.clear();
        }
        _recursivelyFission(cell, newCellsOutput);

    }

    private static void _recursivelyFission(AdaptiveCell cell, Collection<AdaptiveCell> newCell) {
        do {
            if (cell.isAbleToFission()) {
                cell.fission();
                if (null != newCell) {
                    newCell.addAll(Arrays.asList(cell.getChildren()));
                }
                break;
            } else {
                _recursivelyFission(cell.searchFissionObstrutor(), newCell);
            }
        } while (true);
    }

    public static boolean isPointRestrictlyInsideCell(AdaptiveCell cell, double x, double y) {
        Iterator<AdaptiveCellEdge> edgeIter = new SegmentIterator<>(cell.getCornerEdges()[0]);
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

    public static void linkEdgeAndCell(AdaptiveCell cell) {
        for (AdaptiveCellEdge edge : cell) {
            edge.setCell(cell);
        }
    }

    public static void linkCornerEdges(AdaptiveCell cell) {
        AdaptiveCellEdge[] cornerEdges = cell.getCornerEdges();
        for (int i = 0; i < cornerEdges.length; i++) {
            Segment2DUtils.link(cornerEdges[i], cornerEdges[(i + 1) % cornerEdges.length]);
        }
    }
}
