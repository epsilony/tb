/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface AdaptiveCell extends Iterable<AdaptiveCellEdge>{

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
