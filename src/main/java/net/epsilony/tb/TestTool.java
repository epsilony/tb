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

package net.epsilony.tb;

import net.epsilony.tb.analysis.Math2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.epsilony.tb.solid.Facet;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TestTool {

    public static double[] linSpace(double start, double end, int numPt) {
        double d = end - start;
        double[] result = new double[numPt];
        double numD = numPt - 1;
        for (int i = 0; i < numPt; i++) {
            result[i] = start + d * (i / numD);
        }
        result[result.length - 1] = end;
        return result;
    }

    public static <E> void wash(List<E> list, int time) {
        Random rd = new Random();
        int size = list.size();
        for (int i = 0; i < time; i++) {
            int ri = rd.nextInt(size);
            E e = list.remove(ri);
            ri = rd.nextInt(size);
            list.add(ri, e);
        }
    }

    public static LinkedList<double[]> linSpace2D(double[] start, double[] end, int numPt) {
        double[] parms = TestTool.linSpace(0, 1, numPt);
        LinkedList<double[]> result = new LinkedList<>();
        for (double par : parms) {
            double[] point = Math2D.pointOnSegment(start, end, par, null);
            result.add(point);
        }
        return result;
    }

    public static Facet samplePolygon(List<double[][][]> coordChainsOut) {
        double[][][] coordChains = new double[][][]{{
            {0, 0},//0
            {2, 0},//1
            {2, 2},//2
            {3, 1},//3
            {4, 2},//4
            {5, 2},//5
            {3, 3},//6
            {3, 2},//7
            {2, 4},//8
            {6, 3},//9
            {7, 4},//10
            {7, 2},//11
            {8, 5},//12
            {8, 7},//13
            {7, 7},//14
            {8, 8},//15
            {5, 9},//16
            {6, 6},//17
            {5, 7},//18
            {4, 6},//19
            {4, 8},//20
            {3, 7},//21
            {2, 7},//22
            {3, 6},//23
            {4, 4},//24
            {0, 7},//25
            {2, 3},//26
            {0, 2}//27
        },
        {
            {5, 5},
            {6, 4},
            {5, 4}
        },
        {
            {7.5, 5},
            {7, 5},
            {6.5, 6},
            {7.5, 6}}
        };
        if (null != coordChainsOut) {
            coordChainsOut.clear();
            coordChainsOut.add(coordChains);
        }
        return Facet.byCoordChains(coordChains);
    }
}
