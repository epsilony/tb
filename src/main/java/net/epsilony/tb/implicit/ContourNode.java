/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

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
}
