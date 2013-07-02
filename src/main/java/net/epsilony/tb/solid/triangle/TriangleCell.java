/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.triangle;

import net.epsilony.tb.solid.WingedEdge;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface TriangleCell<CELL extends TriangleCell<CELL, EDGE, ND>, EDGE extends WingedEdge<CELL, EDGE, ND>, ND extends Node> extends Triangle<ND> {

    EDGE getVertexEdge(int i);

    void setVertexEdge(int i, EDGE edge);
}
