/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.Iterator;
import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.winged.AbstractTriangleCell;
import net.epsilony.tb.solid.winged.WingedCell;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCell extends AbstractTriangleCell<TriangleContourCell, TriangleContourCellEdge, ContourNode> implements WingedCell<TriangleContourCell, TriangleContourCellEdge, ContourNode>, Iterable<TriangleContourCellEdge> {

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
            TriangleContourCellEdge edge = getVertexEdge(i);
            double[] funcValues = edge.getStart().getFunctionValue();
            if (funcValues[0] >= contourLevel) {
                status += w;
            }
            w *= 2;
        }
    }

    public TriangleContourCellEdge getContourSourceEdge() {
        if (status < 0) {
            throw new IllegalStateException("status haven't been updated");
        }
        int index = STATUS_CONTOUR_SRC_EDGE_INDEX_MAP[status];
        if (index < 0) {
            return null;
        }
        return getVertexEdge(index);
    }

    public TriangleContourCellEdge getContourDestinationEdge() {
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
        TriangleContourCellEdge opposite = getVertexEdge(index).getOpposite();
        if (null == opposite) {
            return null;
        }
        return (TriangleContourCell) opposite.getCell();
    }

    @Override
    public Iterator<TriangleContourCellEdge> iterator() {
        return new Iterator<TriangleContourCellEdge>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < getNumberOfVertes();
            }

            @Override
            public TriangleContourCellEdge next() {
                return getVertexEdge(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
