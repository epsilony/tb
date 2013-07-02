/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveCellEdge extends Line2D {

    AdaptiveCellEdge opposite;
    AdaptiveCell cell;

    public AdaptiveCell getCell() {
        return cell;
    }

    public void setCell(AdaptiveCell cell) {
        this.cell = cell;
    }

    public AdaptiveCellEdge getOpposite() {
        return opposite;
    }

    public void setOpposite(AdaptiveCellEdge opposite) {
        this.opposite = opposite;
    }

    @Override
    public void bisect() {
        AdaptiveCellEdge newSucc = new AdaptiveCellEdge();
        newSucc.setStart(new Node(Segment2DUtils.chordMidPoint(this, null)));
        newSucc.setSucc(getSucc());
        newSucc.setPred(this);
        getSucc().setPred(newSucc);
        setSucc(newSucc);
        newSucc.setCell(cell);

        if (null == opposite) {
            return;
        }

        AdaptiveCellEdge newOpposite = new AdaptiveCellEdge();
        newOpposite.setStart(getEnd());
        newOpposite.setSucc(opposite.getSucc());
        newOpposite.setPred(opposite);
        opposite.getSucc().setPred(newOpposite);
        opposite.setSucc(newOpposite);

        newOpposite.setOpposite(this);
        opposite.setOpposite(getSucc());
        getSucc().setOpposite(opposite);
        newOpposite.setCell(opposite.getCell());
        opposite = newOpposite;
    }

    @Override
    public AdaptiveCellEdge getPred() {
        return (AdaptiveCellEdge) super.getPred();
    }

    @Override
    public AdaptiveCellEdge getSucc() {
        return (AdaptiveCellEdge) super.getSucc();
    }

    @Override
    public void setPred(Line2D pred) {
        AdaptiveCellEdge wPred = (AdaptiveCellEdge) pred;
        super.setPred(wPred);

    }

    @Override
    public void setSucc(Line2D succ) {
        AdaptiveCellEdge wSucc = (AdaptiveCellEdge) succ;
        super.setSucc(wSucc);
    }
}
