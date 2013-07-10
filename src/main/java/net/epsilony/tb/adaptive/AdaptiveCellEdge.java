/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.AbstractWingedEdge;
import net.epsilony.tb.solid.winged.WingedEdge;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveCellEdge<ND extends Node & Factory<ND>> extends AbstractWingedEdge<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND> implements WingedEdge<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND>{

    public void bisect() {
        AdaptiveCellEdge<ND> newSucc = new AdaptiveCellEdge<>();
        ND newNode = getStart().produce();
        newNode.setCoord(Segment2DUtils.chordMidPoint(this, null));
        newSucc.setStart(newNode);
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
}
