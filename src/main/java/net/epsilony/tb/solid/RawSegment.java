/* (c) Copyright by Man YUAN */
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
