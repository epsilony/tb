/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import java.util.Iterator;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.SegmentIterator;
import net.epsilony.tb.solid.winged.AbstractWingedCell;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractAdaptiveCell extends AbstractWingedCell implements AdaptiveCell {

    public static int defaultMaxSideSizePower = 1;
    ArrayList<AdaptiveCell> children;
    int level = 0;
    int maxSideSizePower = defaultMaxSideSizePower;

    protected void bisectEdges() {
        for (int i = 0; i < getNumberOfVertes(); i++) {
            if (getVertexEdge(i).getSucc() == getVertexEdge((i + 1) % getNumberOfVertes())) {
                getVertexEdge(i).bisect();
            }
        }
    }

    @Override
    public void fission() {
        if (!isAbleToFission()) {
            throw new IllegalStateException("Cannot fission!" + this);
        }
        bisectEdges();
        ArrayList<AdaptiveCellEdge> midEdges = getEdgesEndBySideMids();
        genChildren(midEdges);
    }

    protected abstract void genChildren(ArrayList<AdaptiveCellEdge> midEdges);

    @Override
    public ArrayList<AdaptiveCell> getChildren() {
        return children;
    }

    protected ArrayList<AdaptiveCellEdge> getEdgesEndBySideMids() {
        ArrayList<AdaptiveCellEdge> result = new ArrayList<>(getNumberOfVertes());
        int maxSideSize = getMaxSideSize();
        for (int side = 0; side < getNumberOfVertes(); side++) {
            AdaptiveCellEdge edgeA = (AdaptiveCellEdge) getVertexEdge(side);
            AdaptiveCellEdge edgeB = (AdaptiveCellEdge) getVertexEdge((side + 1) % getNumberOfVertes());
            double[] sideStart = edgeA.getStart().getCoord();
            double[] sideEnd = edgeB.getStart().getCoord();
            double[] midCoord = Math2D.pointOnSegment(sideStart, sideEnd, 0.5, null);
            AdaptiveCellEdge edge = edgeA;
            double lenSq = Math2D.distanceSquare(edge.getStart().getCoord(), edge.getEnd().getCoord());
            int count = 0;
            while (edge != edgeB) {
                AdaptiveCellEdge edgeSucc = (AdaptiveCellEdge) edge.getSucc();
                double lenSqSucc = Math2D.distanceSquare(edgeSucc.getStart().getCoord(), edgeSucc.getEnd().getCoord());
                if (Math2D.distanceSquare(midCoord, edge.getEnd().getCoord()) < 0.2 * Math.min(lenSq, lenSqSucc)) {
                    result.add(edge);
                }
                edge = edgeSucc;
                lenSq = lenSqSucc;
                count++;
                if (count > maxSideSize) {
                    throw new IllegalStateException();
                }
            }
        }
        return result;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxSideSize() {
        return (int) Math.pow(2, maxSideSizePower);
    }

    @Override
    public int getMaxSideSizePower() {
        return maxSideSizePower;
    }

    @Override
    public boolean isAbleToFission() {
        return null == searchFissionObstrutor();
    }

    @Override
    public Iterator<AdaptiveCellEdge> iterator() {
        if (null != getChildren()) {
            return new SegmentIterator<>(null);
        }
        return new SegmentIterator<>((AdaptiveCellEdge) getVertexEdge(0));
    }

    @Override
    public AdaptiveCell searchFissionObstrutor() {
        Iterator<AdaptiveCellEdge> iter = new SegmentIterator<>((AdaptiveCellEdge) getVertexEdge(0));
        AdaptiveCell result = null;
        while (iter.hasNext()) {
            AdaptiveCellEdge edge = iter.next();
            AdaptiveCellEdge edgeOpposite = (AdaptiveCellEdge) edge.getOpposite();
            if (null == edgeOpposite) {
                continue;
            }
            AdaptiveCell oppositeCell = (AdaptiveCell) edgeOpposite.getCell();
            if (null == oppositeCell) {
                continue;
            }
            if (getLevel() - oppositeCell.getLevel() >= getMaxSideSizePower()) {
                return oppositeCell;
            }
        }
        return result;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setMaxSideSizePower(int maxSizeSizePower) {
        this.maxSideSizePower = maxSizeSizePower;
    }
}
