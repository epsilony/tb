/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SimpTriangleCell<ND extends Node> extends AbstractTriangleCell<SimpTriangleCell<ND>, SimpTriangleEdge<ND>, ND> {

    public static <N extends Node> Factory<SimpTriangleCell<N>> factory() {
        return new Factory<SimpTriangleCell<N>>() {
            @Override
            public SimpTriangleCell<N> produce() {
                return new SimpTriangleCell<>();
            }
        };
    }

    @Override
    public int getNumberOfVertes() {
        return 3;
    }
}
