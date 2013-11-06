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

package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RawSegment extends RawGeomUnit implements Segment {

    protected int diffOrder = 0;
    protected Node start;
    protected Segment pred;
    protected Segment succ;

    public RawSegment() {
    }

    public RawSegment(Node start) {
        this.start = start;
        start.setParent(parent);
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public Node getStart() {
        return start;
    }

    public double[] getStartCoord() {
        return start.coord;
    }

    @Override
    public Segment getPred() {
        return pred;
    }

    @Override
    public Node getEnd() {
        return succ.getStart();
    }

    public double[] getEndCoord() {
        return getEnd().coord;
    }

    @Override
    public Segment getSucc() {
        return succ;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new UnsupportedOperationException("Only support 0 and 1, not :" + diffOrder);
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public void setStart(Node start) {
        this.start = start;
        start.setParent(this);
    }

    @Override
    public void setPred(Segment pred) {
        this.pred = pred;
    }

    @Override
    public void setSucc(Segment succ) {
        this.succ = succ;
    }

    @Override
    public double[] values(double x, double[] results) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bisect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
