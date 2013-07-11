/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Line2D extends AbstractLine2D<Line2D, Node> {

    public Line2D() {
    }

    public Line2D(Node start) {
        this.start = start;
    }

    public void bisect() {
        Line2D newSucc = newInstance();
        newSucc.setStart(bisectionNode());
        newSucc.succ = this.succ;
        newSucc.pred = this;
        this.succ.setPred(newSucc);
        this.succ = newSucc;
    }

    protected Node bisectionNode() {
        return new Node(Segment2DUtils.chordMidPoint(this, null));
    }

    @Override
    public String toString() {
        String endStr = (null == succ || null == getEnd()) ? "NULL" : getEnd().toString();
        String startStr = (null == start) ? "NULL" : start.toString();
        return String.format("Segment2D(%d)[h:(%s), r:(%s)]", id, startStr, endStr);
    }

    protected Line2D newInstance() {
        return new Line2D();
    }
}
