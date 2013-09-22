/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterable;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourCellBoundaryUtil {

    List<TriangleContourCell> cells;
    List<Line> contourHeads;

    public List<TriangleContourCell> getCells() {
        return cells;
    }

    public void setCells(List<TriangleContourCell> cells) {
        this.cells = cells;
    }

    public List<Line> getContourHeads() {
        return contourHeads;
    }

    public void setContourHeads(List<Line> contourHeads) {
        this.contourHeads = contourHeads;
    }

    public List<Line> genInnerTriangleBoundariesHeads() {
        Set<TriangleContourCell> inners = getInnerTriangles();
        List<Line> heads = genHeadsOfBoundaries(inners);
        treatComplexChains(heads);
        return heads;
    }

    private Set<TriangleContourCell> getInnerTriangles() {
        Set<TriangleContourCell> inners = new HashSet<>();
        for (TriangleContourCell cell : cells) {
            if ((cell.passByContourLines == null || cell.passByContourLines.isEmpty())
                    && cell.getStatus() == 0) {
                inners.add(cell);
            }
        }
        return inners;
    }

    private List<Line> genHeadsOfBoundaries(Set<TriangleContourCell> inners) {
        Map<Node, Line> asStart = new HashMap<>();
        Map<Node, Line> asEnd = new HashMap<>();
        Set<Line> lines = new HashSet<>();
        for (TriangleContourCell cell : inners) {
            for (int i = 0; i < cell.getNumberOfVertes(); i++) {
                TriangleContourCellEdge edge = cell.getVertexEdge(i);
                if (inners.contains((TriangleContourCell) edge.getOpposite().getCell())) {
                    continue;
                }
                boolean linked = false;
                Line line = new Line(edge.getEnd());
                Line succ = asStart.get(edge.getStart());
                if (succ == null) {
                    asEnd.put(edge.getStart(), line);
                } else {
                    Segment2DUtils.link(line, succ);
                    linked = true;
                    asStart.remove(edge.getStart());
                }
                Line pred = asEnd.get(edge.getEnd());
                if (pred == null) {
                    asStart.put(edge.getEnd(), line);
                } else {
                    Segment2DUtils.link(pred, line);
                    linked = true;
                    asEnd.remove(edge.getEnd());
                }
                if (!linked) {
                    lines.add(line);
                }
            }
        }
        List<Line> heads = pickHeads(lines);
        return heads;
    }

    private void treatComplexChains(List<Line> heads) {
        Map<Node, Line> asStart = new HashMap<>();
        Map<Node, List<Line>> conflictsMap = new HashMap<>();
        for (Line head : heads) {
            for (Line line : new SegmentIterable<>(head)) {
                Line conflictLine = asStart.get(line.getStart());
                if (null != conflictLine) {
                    List<Line> conflicts = conflictsMap.get(line.getStart());
                    if (null == conflicts) {
                        conflicts = new ArrayList<>(3);
                        conflictsMap.put(line.getStart(), conflicts);
                        conflicts.add(conflictLine);
                    }
                    conflicts.add(line);
                } else {
                    asStart.put(line.getStart(), line);
                }
            }
        }
        if (conflictsMap.isEmpty()) {
            return;
        }

        boolean treated = false;
        for (Entry<Node, List<Line>> entry : conflictsMap.entrySet()) {
            List<Line> lines = entry.getValue();
            switch (lines.size()) {
                case 2: {
                    Line l = lines.get(0);
                    Line lp = (Line) l.getPred();
                    double dot = Segment2DUtils.chordVectorDot(l, lp);
                    if (dot < 0) {
                        continue;
                    }
                    Line ll = lines.get(1);
                    Line llp = (Line) ll.getPred();
                    Segment2DUtils.link(llp, l);
                    Segment2DUtils.link(lp, ll);
                    treated = true;
                }
                break;
                case 3: {
                    Line[] preds = new Line[3];
                    for (int i = 0; i < 3; i++) {
                        Line l = lines.get(i);
                        Line lp = (Line) l.getPred();
                        if (Segment2DUtils.chordVectorDot(l, lp) < 0) {
                            preds[i] = lp;
                        } else {
                            treated = true;
                            for (int j = 1; j < 3; j++) {
                                Line lpp = (Line) lines.get((i + j) % 3).getPred();
                                if (Segment2DUtils.chordVectorDot(l, lpp) < 0) {
                                    preds[i] = lpp;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        Segment2DUtils.link(preds[i], lines.get(i));
                    }
                }
                break;
                default:
                    throw new IllegalStateException();
            }
        }

        if (treated) {
            List<Line> result = pickHeads(new HashSet<>(heads));
            heads.clear();
            heads.addAll(result);
        }

    }

    private List<Line> pickHeads(Set<Line> lines) {
        List<Line> heads = new LinkedList<>();
        while (!lines.isEmpty()) {
            Line headCandidate = lines.iterator().next();
            lines.remove(headCandidate);
            heads.add(headCandidate);
            SegmentIterator<Line> lineIterator = new SegmentIterator<>(headCandidate);
            while (lineIterator.hasNext()) {
                lines.remove(lineIterator.next());
            }
        }
        return heads;
    }
}
