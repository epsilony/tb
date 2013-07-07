/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SimpTriangleEdge<ND extends Node> extends AbstractWingedEdge<SimpTriangleCell<ND>, SimpTriangleEdge<ND>, ND> {

    public static <N extends Node> Factory<SimpTriangleEdge<N>> factory() {
        return new Factory<SimpTriangleEdge<N>>() {
            @Override
            public SimpTriangleEdge<N> produce() {
                return new SimpTriangleEdge<>();
            }
        };
    }
}
