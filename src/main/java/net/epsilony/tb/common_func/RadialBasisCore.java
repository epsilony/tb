/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import net.epsilony.tb.analysis.WithDiffOrder;
import net.epsilony.tb.CloneFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface RadialBasisCore extends WithDiffOrder, CloneFactory<RadialBasisCore> {

    double[] valuesByDistance(double distance, double[] result);

    double[] valuesByDistanceSquare(double distanceSquare, double[] results);
}
