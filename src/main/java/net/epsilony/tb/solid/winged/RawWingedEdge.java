/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.RawSegment;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RawWingedEdge extends RawSegment implements WingedEdge {

    protected WingedEdge opposite;
    protected WingedCell cell;

    @Override
    public WingedEdge getOpposite() {
        return opposite;
    }

    @Override
    public void setOpposite(WingedEdge opposite) {
        this.opposite = opposite;
    }

    @Override
    public WingedCell getCell() {
        return cell;
    }

    @Override
    public void setCell(WingedCell cell) {
        this.cell = cell;
    }
}
