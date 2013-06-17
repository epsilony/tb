/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface BoundedImplicitFunctionSolver extends ImplicitFunctionSolver {

    void setLowerBounds(double[] lowerBounds);

    void setUpperBounds(double[] upperBounds);
    
}
