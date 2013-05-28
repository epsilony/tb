/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.tb.adaptive;

/**
 *
 * @author epsilon
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
