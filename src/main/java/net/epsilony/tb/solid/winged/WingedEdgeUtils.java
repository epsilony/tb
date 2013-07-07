/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedEdgeUtils {

    public static <CELL extends WingedCell<CELL, EDGE, NODE>, EDGE extends WingedEdge<CELL, EDGE, NODE>, NODE extends Node> //
            void linkAsOpposite(EDGE edgeA, EDGE edgeB) {
        edgeA.setOpposite(edgeB);
        edgeB.setOpposite(edgeA);
    }
}
