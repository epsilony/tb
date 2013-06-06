/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.IntIdentityMap;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface TriangleContourBuilder {

    List<TriangleContourCell> getCells();

    double getContourLevel();

    DifferentiableFunction<double[], double[]> getLevelSetFunction();

    NewtonSolver getNewtonSolver();

    void setCells(List<TriangleContourCell> cells);

    void setContourLevel(double contourLevel);

    void setLevelSetFunction(DifferentiableFunction<double[], double[]> levelSetFunction);

    void setNewtonSolver(NewtonSolver newtonSolver);

    void genContour();

    List<Line2D> getContourHeads();
}
