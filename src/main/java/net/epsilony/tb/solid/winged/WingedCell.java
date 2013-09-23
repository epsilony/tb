/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface WingedCell extends Triangle<Node> {

    WingedEdge getVertexEdge(int i);

    void setVertexEdge(int i, WingedEdge edge);

    int getNumberOfVertes();
}
