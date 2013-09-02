/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import java.io.Serializable;
import net.epsilony.tb.analysis.Dimensional;
import net.epsilony.tb.analysis.WithDiffOrder;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface BasesFunction extends Dimensional, WithDiffOrder, Serializable {

    double[][] values(double[] vec, double[][] output);

    int basesSize();
}
