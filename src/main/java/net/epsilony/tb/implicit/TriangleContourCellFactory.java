/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.winged.RawWingedEdge;
import net.epsilony.tb.solid.winged.RectangleCoverTriangleCellsFactory;
import net.epsilony.tb.solid.winged.WingedEdge;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellFactory extends RectangleCoverTriangleCellsFactory {

    public TriangleContourCellFactory() {
        setCellFactory(new Factory<TriangleContourCell>() {
            @Override
            public TriangleContourCell produce() {
                return new TriangleContourCell();
            }
        });
        setEdgeFactory(new Factory<WingedEdge>() {
            @Override
            public WingedEdge produce() {
                return new RawWingedEdge();
            }
        });
        setNodeFactory(ContourNode.contourNodeFactory());
    }
}
