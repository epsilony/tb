/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleArrayContainers<TRI extends Triangle<ND>, ND extends Node> {

    ArrayList<TRI> triangles;
    ArrayList<ND> nodes;
}
