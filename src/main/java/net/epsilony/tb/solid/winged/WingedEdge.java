/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Segment;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface WingedEdge extends Segment {

    WingedEdge getOpposite();

    void setOpposite(WingedEdge opposite);

    WingedCell getCell();

    void setCell(WingedCell cell);
}
