/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Line2D<ND extends Node> extends AbstractLine2D<Line2D<ND>, ND> {

    public Line2D() {
    }

    public Line2D(ND start) {
        super(start);
    }

    public void bisect() {
        Line2D<ND> newSucc = new Line2D<>();
        newSucc.setStart(bisectionNode());
        newSucc.succ = this.succ;
        newSucc.pred = this;
        this.succ.setPred(newSucc);
        this.succ = newSucc;
    }

    protected ND bisectionNode() {
        ND newNode=Node.instanceByClass(start);
        newNode.setCoord(Segment2DUtils.chordMidPoint(this, null));
        return newNode;
    }

    @Override
    public String toString() {
        String endStr = (null == succ || null == getEnd()) ? "NULL" : getEnd().toString();
        String startStr = (null == start) ? "NULL" : start.toString();
        return String.format("Line2D(%d)[h:(%s), r:(%s)]", id, startStr, endStr);
    }
}
