/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.RudeFactory;
import net.epsilony.tb.solid.winged.RawWingedEdge;
import net.epsilony.tb.solid.winged.RectangleCoverTriangleCellsFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellFactory extends RectangleCoverTriangleCellsFactory {

    public TriangleContourCellFactory() {
        setCellFactory(new RudeFactory<>(TriangleContourCell.class));
        setEdgeFactory(new RudeFactory<>(RawWingedEdge.class));
        setNodeFactory(ContourNode.contourNodeFactory());
    }
}
