/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import net.epsilony.tb.analysis.WithDiffOrder;
import net.epsilony.tb.synchron.SynchronizedClonable;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface RadialFunctionCore extends WithDiffOrder, SynchronizedClonable<RadialFunctionCore> {

    double[] valuesByDistance(double distance, double[] result);
}
