/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a> St-Pierre</a>
 */
public class SingleLine<ND extends Node> extends AbstractLine<SingleLine<ND>, ND> {

    ND end;

    public void setEnd(ND end) {
        this.end = end;
    }

    @Override
    public ND getEnd() {
        return end;
    }

    @Override
    public SingleLine<ND> getPred() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SingleLine<ND> getSucc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPred(SingleLine<ND> pred) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSucc(SingleLine<ND> succ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
