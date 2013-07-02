/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.triangle;

import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface Triangle<ND extends Node> {

    void setVertex(int index, ND vertes);

    ND getVertex(int index);
}
