/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.Arrays;
import java.util.Collection;
import net.epsilony.tb.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveUtils {

    public static void fission(AdaptiveCell cell, boolean recursively, Collection<AdaptiveCell> newCellsOutput) {
        if (recursively) {
            recursivelyFission(cell, newCellsOutput);
        } else if (cell.isAbleToFissionToChildren()) {
            cell.fissionToChildren();
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
            if (cell.isAbleToFissionToChildren()) {
                cell.fissionToChildren();
                if (null != newCell) {
                    newCell.addAll(Arrays.asList(cell.getChildren()));
                }
                break;
            } else {
                _recursivelyFission(cell.findOneFissionObstrutor(), newCell);
            }
        } while (true);
    }

    public static boolean isPointRestrictlyInsideCell(AdaptiveCell cell, double x, double y) {
        for (AdaptiveCellEdge edge : cell.getEdges()) {
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
    
    public static void link(AdaptiveCellEdge e1,AdaptiveCellEdge e2){
        e1.addOpposite(e2);
        e2.addOpposite(e1);
    }
}
