/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.winged.AbstractWingedEdge;
import net.epsilony.tb.solid.winged.WingedEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class AdaptiveCellEdge<ND extends Node> extends AbstractWingedEdge<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND> implements WingedEdge<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND> {

    public static Logger logger = LoggerFactory.getLogger(AdaptiveCellEdge.class);

    public void bisect() {
        AdaptiveCellEdge<ND> newSucc = new AdaptiveCellEdge<>();
        ND newNode;
        try {
            newNode = (ND) getStart().getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("ND does not have a null constructor", ex);
            throw new IllegalStateException(ex);
        }
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
