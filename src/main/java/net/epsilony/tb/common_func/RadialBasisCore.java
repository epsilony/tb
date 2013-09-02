/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import java.io.Serializable;
import net.epsilony.tb.analysis.WithDiffOrder;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface RadialBasisCore extends WithDiffOrder, Serializable {

    double[] valuesByDistance(double distance, double[] result);

    double[] valuesByDistanceSquare(double distanceSquare, double[] results);
}
