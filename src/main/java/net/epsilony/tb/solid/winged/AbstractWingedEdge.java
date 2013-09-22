/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.AbstractLine;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractWingedEdge<CELL extends WingedCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> extends AbstractLine<EDGE, ND> implements WingedEdge<CELL, EDGE, ND> {

    protected EDGE opposite;
    protected CELL cell;

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
