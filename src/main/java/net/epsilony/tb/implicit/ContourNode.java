/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ContourNode extends Node {

    double[] functionValue;

    public double[] getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(double[] functionValue) {
        this.functionValue = functionValue;
    }

    public static double gradientCos(ContourNode ndA, ContourNode ndB) {
        double[] funa = ndA.functionValue;
        double[] funb = ndB.functionValue;
        double gax = funa[1];
        double gay = funa[2];
        double gbx = funb[1];
        double gby = funb[2];
        return Math2D.cos(gax, gay, gbx, gby);
    }
}
