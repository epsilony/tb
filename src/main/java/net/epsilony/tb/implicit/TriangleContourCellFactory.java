/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.winged.RectangleCoverTriangleCellsFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellFactory extends RectangleCoverTriangleCellsFactory<TriangleContourCell, TriangleContourCellEdge, ContourNode> {

    public TriangleContourCellFactory() {
        setCellFactory(new Factory<TriangleContourCell>() {
            @Override
            public TriangleContourCell produce() {
                return new TriangleContourCell();
            }
        });
        setEdgeFactory(new Factory<TriangleContourCellEdge>() {
            @Override
            public TriangleContourCellEdge produce() {
                return new TriangleContourCellEdge();
            }
        });
        setNodeFactory(ContourNode.contourNodeFactory());
    }
}
