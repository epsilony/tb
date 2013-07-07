/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.GeneralLine2D;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractWingedEdge<CELL, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> extends GeneralLine2D<EDGE, ND> implements WingedEdge<CELL, EDGE, ND> {

    EDGE opposite;
    CELL cell;

    @Override
    public EDGE getOpposite() {
        return opposite;
    }

    @Override
    public void setOpposite(EDGE opposite) {
        this.opposite = opposite;
    }

    @Override
    public CELL getCell() {
        return cell;
    }

    @Override
    public void setCell(CELL cell) {
        this.cell = cell;
    }
}
