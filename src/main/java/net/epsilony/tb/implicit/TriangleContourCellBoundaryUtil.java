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
import net.epsilony.tb.adaptive.AdaptiveCellEdge;
import net.epsilony.tb.solid.Line2D;
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
    List<Line2D> contourHeads;

    public List<TriangleContourCell> getCells() {
        return cells;
    }

    public void setCells(List<TriangleContourCell> cells) {
        this.cells = cells;
    }

    public List<Line2D> getContourHeads() {
        return contourHeads;
    }

    public void setContourHeads(List<Line2D> contourHeads) {
        this.contourHeads = contourHeads;
    }

    public List<Line2D> genInnerTriangleBoundariesHeads() {
        Set<TriangleContourCell> inners = getInnerTriangles();
        List<Line2D> heads = genHeadsOfBoundaries(inners);
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

    private List<Line2D> genHeadsOfBoundaries(Set<TriangleContourCell> inners) {
        Map<Node, Line2D> asStart = new HashMap<>();
        Map<Node, Line2D> asEnd = new HashMap<>();
        Set<Line2D> lines = new HashSet<>();
        for (TriangleContourCell cell : inners) {
            for (AdaptiveCellEdge edge : cell) {
                if (inners.contains((TriangleContourCell) edge.getOpposite().getCell())) {
                    continue;
                }
                boolean linked = false;
                Line2D line = new Line2D(edge.getEnd());
                Line2D succ = asStart.get(edge.getStart());
                if (succ == null) {
                    asEnd.put(edge.getStart(), line);
                } else {
                    Segment2DUtils.link(line, succ);
                    linked = true;
                    asStart.remove(edge.getStart());
                }
                Line2D pred = asEnd.get(edge.getEnd());
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
        List<Line2D> heads = pickHeads(lines);
        return heads;
    }

    private void treatComplexChains(List<Line2D> heads) {
        Map<Node, Line2D> asStart = new HashMap<>();
        Map<Node, List<Line2D>> conflictsMap = new HashMap<>();
        for (Line2D head : heads) {
            for (Line2D line : new SegmentIterable<>(head)) {
                Line2D conflictLine = asStart.get(line.getStart());
                if (null != conflictLine) {
                    List<Line2D> conflicts = conflictsMap.get(line.getStart());
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
        for (Entry<Node, List<Line2D>> entry : conflictsMap.entrySet()) {
            List<Line2D> lines = entry.getValue();
            switch (lines.size()) {
                case 2: {
                    Line2D l = lines.get(0);
                    Line2D lp = (Line2D) l.getPred();
                    double dot = Segment2DUtils.chordVectorDot(l, lp);
                    if (dot < 0) {
                        continue;
                    }
                    Line2D ll = lines.get(1);
                    Line2D llp = (Line2D) ll.getPred();
                    Segment2DUtils.link(llp, l);
                    Segment2DUtils.link(lp, ll);
                    treated = true;
                }
                break;
                case 3: {
                    Line2D[] preds = new Line2D[3];
                    for (int i = 0; i < 3; i++) {
                        Line2D l = lines.get(i);
                        Line2D lp = (Line2D) l.getPred();
                        if (Segment2DUtils.chordVectorDot(l, lp) < 0) {
                            preds[i] = lp;
                        } else {
                            treated = true;
                            for (int j = 1; j < 3; j++) {
                                Line2D lpp = (Line2D) lines.get((i + j) % 3).getPred();
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
            List<Line2D> result = pickHeads(new HashSet<>(heads));
            heads.clear();
            heads.addAll(result);
        }

    }

    private List<Line2D> pickHeads(Set<Line2D> lines) {
        List<Line2D> heads = new LinkedList<>();
        while (!lines.isEmpty()) {
            Line2D headCandidate = lines.iterator().next();
            lines.remove(headCandidate);
            heads.add(headCandidate);
            SegmentIterator<Line2D> lineIterator = new SegmentIterator<>(headCandidate);
            while (lineIterator.hasNext()) {
                lines.remove(lineIterator.next());
            }
        }
        return heads;
    }
}
