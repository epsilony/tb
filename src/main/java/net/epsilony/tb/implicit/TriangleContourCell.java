/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Iterator;
import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.winged.TriangleCell;
import net.epsilony.tb.solid.winged.WingedEdge;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCell extends TriangleCell implements Iterable<WingedEdge> {

    private static final int[] STATUS_CONTOUR_DEST_EDGE_INDEX_MAP = new int[]{-1, 0, 1, 1, 2, 0, 2, -1};
    private static final int[] STATUS_CONTOUR_SRC_EDGE_INDEX_MAP = new int[]{-1, 2, 0, 2, 1, 1, 0, -1};
    boolean visited = false;
    private int status = -1;
    List<Line> passByContourLines;

    public int getStatus() {
        return status;
    }

    public List<Line> getPassByContourLines() {
        return passByContourLines;
    }

    public void getPassByContourLines(List<Line> passBySegs) {
        this.passByContourLines = passBySegs;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean polygonized) {
        this.visited = polygonized;
    }

    public void updateStatus(double contourLevel) {
        double w = 1;
        status = 0;
        for (int i = 0; i < getNumberOfVertes(); i++) {
            WingedEdge edge = getVertexEdge(i);
            double[] funcValues = ((ContourNode) edge.getStart()).getFunctionValue();
            if (funcValues[0] >= contourLevel) {
                status += w;
            }
            w *= 2;
        }
    }

    public WingedEdge getContourSourceEdge() {
        if (status < 0) {
            throw new IllegalStateException("status haven't been updated");
        }
        int index = STATUS_CONTOUR_SRC_EDGE_INDEX_MAP[status];
        if (index < 0) {
            return null;
        }
        return getVertexEdge(index);
    }

    public WingedEdge getContourDestinationEdge() {
        if (status < 0) {
            throw new IllegalStateException("status haven't been updated");
        }
        int index = STATUS_CONTOUR_DEST_EDGE_INDEX_MAP[status];
        if (index < 0) {
            return null;
        }
        return getVertexEdge(index);
    }

    public TriangleContourCell nextContourCell() {
        if (status < 0) {
            throw new IllegalStateException("status haven't been updated");
        }
        int index = STATUS_CONTOUR_DEST_EDGE_INDEX_MAP[status];
        if (index < 0) {
            return null;
        }
        WingedEdge opposite = getVertexEdge(index).getOpposite();
        if (null == opposite) {
            return null;
        }
        return (TriangleContourCell) opposite.getCell();
    }

    @Override
    public Iterator<WingedEdge> iterator() {
        return new Iterator<WingedEdge>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < getNumberOfVertes();
            }

            @Override
            public WingedEdge next() {
                return getVertexEdge(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
