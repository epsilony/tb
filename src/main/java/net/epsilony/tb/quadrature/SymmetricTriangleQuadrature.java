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

package net.epsilony.tb.quadrature;

import java.util.Iterator;
import net.epsilony.tb.analysis.ArrvarFunction;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.Triangle;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SymmetricTriangleQuadrature implements Iterable<QuadraturePoint> {

    Triangle<? extends Node> triangle;
    int degree = 2;

    public int getNumberOfQuadraturePoints() {
        return SymmetricTriangleQuadratureUtils.numPointsByAlgebraicAccuracy(degree);
    }

    public void setTriangle(Triangle<? extends Node> triangle) {
        this.triangle = triangle;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public QuadraturePoint getQuadraturePoint(int index) {
        QuadraturePoint result = new QuadraturePoint(2);
        SymmetricTriangleQuadratureUtils.cartesianCoordinate(triangle, degree, index, result.coord);
        result.weight = Math2D.triangleArea(triangle) * SymmetricTriangleQuadratureUtils.getWeight(degree, index);
        return result;
    }

    public double quadrate(ArrvarFunction fun) {
        double result = 0;
        for (QuadraturePoint qp : this) {
            result += qp.weight * fun.value(qp.coord);
        }
        return result;
    }

    @Override
    public Iterator<QuadraturePoint> iterator() {

        return new Iterator<QuadraturePoint>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < getNumberOfQuadraturePoints();
            }

            @Override
            public QuadraturePoint next() {
                return getQuadraturePoint(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); // To
                                                                               // change
                                                                               // body
                                                                               // of
                                                                               // generated
                                                                               // methods,
                                                                               // choose
                                                                               // Tools
                                                                               // |
                                                                               // Templates.
            }
        };
    }
}
