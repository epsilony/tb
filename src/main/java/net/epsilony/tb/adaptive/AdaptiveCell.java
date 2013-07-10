/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.Factory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.WingedCell;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface AdaptiveCell<ND extends Node&Factory<ND>> extends Iterable<AdaptiveCellEdge<ND>>, WingedCell<AdaptiveCell<ND>,AdaptiveCellEdge<ND>,ND>{

    void fission();

    AdaptiveCell[] getChildren();

    int getLevel();

    int getMaxSideSize();

    int getMaxSideSizePower();

    boolean isAbleToFission();

    AdaptiveCell searchFissionObstrutor();

    void setLevel(int level);

    void setMaxSideSizePower(int maxSizeSizePower);

    AdaptiveCellEdge[] getCornerEdges();

    void setCornerEdges(AdaptiveCellEdge[] cornerEdges);

    int getSideNum();
    
}
