/* (c) Copyright by Man YUAN */
package net.epsilony.tb.analysis;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface DifferentiableFunction<IN, OUT> extends GenericFunction<IN, OUT>, WithDiffOrder {

    int getInputDimension();

    int getOutputDimension();
}
