/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import net.epsilony.tb.analysis.Dimensional;
import net.epsilony.tb.analysis.WithDiffOrder;
import net.epsilony.tb.CloneFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface BasesFunction<T extends BasesFunction> extends Dimensional,WithDiffOrder , CloneFactory<T>{

    double[][] values(double[] vec, double[][] output);

    int basesSize();
}
