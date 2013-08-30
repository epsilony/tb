/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a> St-Pierre</a>
 */
public class SingleLine2D<ND extends Node> extends AbstractLine2D<SingleLine2D<ND>, ND> {

    ND end;

    public void setEnd(ND end) {
        this.end = end;
    }

    @Override
    public ND getEnd() {
        return end;
    }

    @Override
    public SingleLine2D<ND> getPred() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SingleLine2D<ND> getSucc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPred(SingleLine2D<ND> pred) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSucc(SingleLine2D<ND> succ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
