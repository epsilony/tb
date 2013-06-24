/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.awt.geom.Rectangle2D;
import net.epsilony.tb.adaptive.TriangleAdaptiveCell;
import net.epsilony.tb.adaptive.TriangleAdaptiveCellFactory;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellFactory extends TriangleAdaptiveCellFactory {

    @Override
    public TriangleContourCell[][] coverRectangle(Rectangle2D rectangle, double edgeLength) {
        TriangleAdaptiveCell[][] coverRectangle = super.coverRectangle(rectangle, edgeLength);
        TriangleContourCell[][] result = new TriangleContourCell[coverRectangle.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = new TriangleContourCell[coverRectangle[i].length];
            System.arraycopy(coverRectangle[i], 0, result[i], 0, result[i].length);
        }
        return result;
    }

    @Override
    protected TriangleContourCell newTriangleAdaptiveCellInstance() {
        return new TriangleContourCell();
    }

    @Override
    protected Node newNodeInstance() {
        return new ContourNode();
    }
}
