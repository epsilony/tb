/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import java.util.List;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleArrayContainers<TRI extends Triangle<ND>, ND extends Node> {

    public ArrayList<TRI> triangles;
    public ArrayList<ND> nodes;
    public List<ND> spaceNodes;
}
