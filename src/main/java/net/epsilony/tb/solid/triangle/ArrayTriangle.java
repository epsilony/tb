/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.triangle;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ArrayTriangle<ND extends Node> implements Triangle<ND> {

    ArrayList<ND> vertes = new ArrayList<>(3);

    public ArrayTriangle() {
        for (int i = 0; i < 3; i++) {
            vertes.add(null);
        }
    }

    @Override
    public void setVertex(int index, ND vertex) {
        vertes.set(index, vertex);
    }

    @Override
    public ND getVertex(int index) {
        return vertes.get(index);
    }
}
