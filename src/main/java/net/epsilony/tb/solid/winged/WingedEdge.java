/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface WingedEdge<CELL, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> extends Segment<EDGE, ND> {

    EDGE getOpposite();

    void setOpposite(EDGE opposite);

    CELL getCell();

    void setCell(CELL cell);
}
