/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.epsilony.tb.implicit;

import net.epsilony.tb.Factory;
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

    public static Factory<ContourNode> contourNodeFactory() {
        return new Factory<ContourNode>() {
            @Override
            public ContourNode produce() {
                return new ContourNode();
            }
        };
    }
}
