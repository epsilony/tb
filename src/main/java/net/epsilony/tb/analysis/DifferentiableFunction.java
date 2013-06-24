/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface DifferentiableFunction extends GenericFunction<double[], double[]>, WithDiffOrder {

    int getInputDimension();

    int getOutputDimension();
}
