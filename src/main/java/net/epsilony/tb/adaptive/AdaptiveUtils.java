/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.Collection;
import java.util.Iterator;
import net.epsilony.tb.analysis.Math2D;
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
                newCellsOutput.addAll(cell.getChildren());
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
                    newCell.addAll(cell.getChildren());
                }
                break;
            } else {
                _recursivelyFission(cell.searchFissionObstrutor(), newCell);
            }
        } while (true);
    }

    public static boolean isPointRestrictlyInsideCell(AdaptiveCell cell, double x, double y) {
        Iterator<AdaptiveCellEdge> edgeIter = new SegmentIterator<>((AdaptiveCellEdge) cell.getVertexEdge(0));
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
}
