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

package net.epsilony.tb.analysis;

import gnu.trove.list.array.TDoubleArrayList;
import org.apache.commons.math3.util.ArithmeticUtils;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WithDiffOrderUtil {

    public static int outputLength(int dim, int diffOrder) {
        switch (dim) {
            case 1:
                return diffOrder + 1;
            case 2:
                return outputLength2D(diffOrder);
            case 3:
                return outputLength3D(diffOrder);
            default:
                throw new IllegalArgumentException("The dim should be 1 or 2, not " + dim);
        }
    }

    public static int outputLength2D(int diffOrder) {
        return (diffOrder + 2) * (diffOrder + 1) / 2;
    }
    private static final int[] OUTPUT_LENGTH_3D = new int[]{1, 4, 10, 20};

    public static int outputLength3D(int diffOrder) {
        if (diffOrder < OUTPUT_LENGTH_3D.length) {
            return OUTPUT_LENGTH_3D[diffOrder];
        }
        int result = OUTPUT_LENGTH_3D[OUTPUT_LENGTH_3D.length - 1];
        for (int i = OUTPUT_LENGTH_3D.length; i <= diffOrder; i++) {
            result += ArithmeticUtils.binomialCoefficient(2 + i, 2);
        }
        return result;
    }

    public static TDoubleArrayList[] initOutput(TDoubleArrayList[] toInit, int capacity, int dim, int diffOrder) {
        int size = outputLength(dim, diffOrder);
        TDoubleArrayList[] result;
        if (null == toInit) {
            result = new TDoubleArrayList[size];
            for (int i = 0; i < result.length; i++) {
                if (capacity > 0) {
                    result[i] = new TDoubleArrayList(capacity);
                }
            }
        } else {
            result = toInit;
            for (int i = 0; i < result.length; i++) {
                result[i].resetQuick();
                if (capacity > 0) {
                    result[i].ensureCapacity(capacity);
                }
            }
        }
        return result;
    }
}
