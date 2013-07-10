/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractTriangleCell<CELL extends AbstractTriangleCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> extends AbstractWingedCell<CELL, EDGE, ND> implements WingedCell<CELL, EDGE, ND> {

    @Override
    public int getNumberOfVertes() {
        return 3;
    }
}
