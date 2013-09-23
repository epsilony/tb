/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleCell extends AbstractWingedCell implements WingedCell, Triangle<Node> {

    @Override
    public int getNumberOfVertes() {
        return 3;
    }
}
