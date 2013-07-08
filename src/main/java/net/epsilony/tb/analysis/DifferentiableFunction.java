/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 * the result of values is \(f,f_1,f_2,\ldots,f_n\)
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface DifferentiableFunction extends GenericFunction<double[], double[]>, WithDiffOrder {

    int getInputDimension();

    int getOutputDimension();
}
