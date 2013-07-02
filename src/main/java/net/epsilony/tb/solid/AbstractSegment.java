/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.IntIdentityMap;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractSegment<PS extends Segment<PS, ND>, ND extends Node> implements Segment<PS, ND> {

    protected int diffOrder = 0;
    protected ND start;
    public int id = IntIdentityMap.NULL_INDEX_SUPREMUM;
    protected PS pred;
    protected PS succ;

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public ND getStart() {
        return start;
    }

    public double[] getStartCoord() {
        return start.coord;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PS getPred() {
        return pred;
    }

    @Override
    public ND getEnd() {
        return succ.getStart();
    }

    public double[] getEndCoord() {
        return getEnd().coord;
    }

    @Override
    public PS getSucc() {
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
    public void setStart(ND start) {
        this.start = start;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setPred(PS pred) {
        this.pred = pred;
    }

    @Override
    public void setSucc(PS succ) {
        this.succ = succ;
    }
}
