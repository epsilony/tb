/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.solid.Line2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface TriangleContourBuilder {

    List<TriangleContourCell> getCells();

    DifferentiableFunction<double[], double[]> getLevelSetFunction();

    void setCells(List<TriangleContourCell> cells);

    void setLevelSetFunction(DifferentiableFunction<double[], double[]> levelSetFunction);

    void genContour();

    List<Line2D> getContourHeads();

    ImplicitFunctionSolver getImplicitFunctionSolver();

    void setImplicitFunctionSolver(ImplicitFunctionSolver implicitFunctionSolver);
}
