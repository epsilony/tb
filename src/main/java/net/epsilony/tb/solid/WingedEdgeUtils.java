/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedEdgeUtils {

    public static <CELL, EDGE extends WingedEdge<CELL, EDGE, NODE>, NODE extends Node> //
            void linkAsOpposite(EDGE edgeA, EDGE edgeB) {
        edgeA.setOpposite(edgeB);
        edgeB.setOpposite(edgeA);
    }
}
