/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import java.util.ArrayList;
import net.epsilony.tb.solid.winged.WingedCell;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface AdaptiveCell extends Iterable<AdaptiveCellEdge>, WingedCell {

    void fission();

    ArrayList<AdaptiveCell> getChildren();

    int getLevel();

    int getMaxSideSize();

    int getMaxSideSizePower();

    boolean isAbleToFission();

    AdaptiveCell searchFissionObstrutor();

    void setLevel(int level);

    void setMaxSideSizePower(int maxSizeSizePower);
}
