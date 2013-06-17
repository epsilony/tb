/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.DifferentiableFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface ConstraintImplicitFunctionSolver extends BoundedImplicitFunctionSolver {

    public void addConstraint(DifferentiableFunction<double[], double[]> constraint, double tolerence);
}
