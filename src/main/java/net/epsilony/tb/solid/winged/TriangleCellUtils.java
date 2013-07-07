/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleCellUtils {

    public static void linkOppositesBySameVertes(WingedCell cellA, WingedCell cellB) {
        WingedEdge edgeA = null, edgeB = null;
        boolean finded = false;
        for (int i = 0; i < 3 && !finded; i++) {
            edgeA = cellA.getVertexEdge(i);
            for (int j = 0; j < 3; j++) {
                edgeB = cellB.getVertexEdge(j);
                if (edgeA.getStart() == edgeB.getEnd() && edgeA.getEnd() == edgeB.getStart()) {
                    finded = true;
                    break;
                }
            }
        }
        if (!finded) {
            throw new IllegalArgumentException();
        }
        WingedEdgeUtils.linkAsOpposite(edgeA, edgeB);
    }
}
