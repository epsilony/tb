/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WingedEdge extends Line2D {

    WingedEdge opposite;

    WingedEdge getOpposite() {
        return opposite;
    }

    void setOpposite(WingedEdge opposite) {
        this.opposite = opposite;
    }

    @Override
    public void bisect() {
        WingedEdge newSucc = new WingedEdge();
        newSucc.setStart(new Node(Segment2DUtils.chordMidPoint(this, null)));
        newSucc.setSucc(getSucc());
        newSucc.setPred(this);
        getSucc().setPred(newSucc);
        setSucc(newSucc);

        if (null == opposite) {
            return;
        }
        
        WingedEdge newOpposite = new WingedEdge();
        newOpposite.setStart(getEnd());
        newOpposite.setSucc(opposite.getSucc());
        newOpposite.setPred(opposite);
        opposite.getSucc().setPred(newOpposite);
        opposite.setSucc(newOpposite);
        
        newOpposite.setOpposite(this);
        opposite.setOpposite(getSucc());
        getSucc().setOpposite(opposite);
        opposite = newOpposite;
    }

    @Override
    public WingedEdge getPred() {
        return (WingedEdge) super.getPred();
    }

    @Override
    public WingedEdge getSucc() {
        return (WingedEdge) super.getSucc();
    }

    @Override
    public void setPred(Segment pred) {
        WingedEdge wPred = (WingedEdge) pred;
        super.setPred(wPred);

    }

    @Override
    public void setSucc(Segment succ) {
        WingedEdge wSucc = (WingedEdge) succ;
        super.setSucc(wSucc);
    }
}
