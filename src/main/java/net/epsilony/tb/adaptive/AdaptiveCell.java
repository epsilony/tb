/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.WingedCell;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface AdaptiveCell<ND extends Node> extends Iterable<AdaptiveCellEdge<ND>>, WingedCell<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND> {

    void fission();

    ArrayList<AdaptiveCell<ND>> getChildren();

    int getLevel();

    int getMaxSideSize();

    int getMaxSideSizePower();

    boolean isAbleToFission();

    AdaptiveCell<ND> searchFissionObstrutor();

    void setLevel(int level);

    void setMaxSideSizePower(int maxSizeSizePower);
}
