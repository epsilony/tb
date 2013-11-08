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

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class DoubleArrayComparator implements Comparator<double[]> {

    final int compareIndex;

    public DoubleArrayComparator(int compareIndex) {
        this.compareIndex = compareIndex;
    }

    @Override
    public int compare(double[] o1, double[] o2) {
        double d1 = o1[compareIndex];
        double d2 = o2[compareIndex];
        if (d1 == d2) {
            return 0;
        } else if (d1 < d2) {
            return -1;
        } else {
            return 1;
        }
    }

    public static ArrayList<DoubleArrayComparator> comparatorsForAll(int size) {
        ArrayList<DoubleArrayComparator> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(new DoubleArrayComparator(i));
        }
        return result;
    }
}
