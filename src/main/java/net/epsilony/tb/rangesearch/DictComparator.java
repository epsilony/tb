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

package net.epsilony.tb.rangesearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class DictComparator<T> implements Comparator<T> {

    ArrayList<Comparator<? super T>> comparators;
    private final int primeComparatorIndex;

    @Override
    public int compare(T o1, T o2) {
        final int size = comparators.size();
        for (int i = 0; i < size; i++) {
            int c = comparators.get((i + primeComparatorIndex) % size).compare(o1, o2);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    public DictComparator(List<? extends Comparator<? super T>> comparators, int primeComparatorIndex) {
        this.comparators = new ArrayList<>(comparators);
        this.primeComparatorIndex = primeComparatorIndex;
    }

    public DictComparator<T> getSlibing(int primeComparatorIndex) {
        return new DictComparator<>(comparators, primeComparatorIndex);
    }

    public int getPrimeComparatorIndex() {
        return primeComparatorIndex;
    }

    public ArrayList<Comparator<? super T>> getComparators() {
        return comparators;
    }

    public int getKeyDimensionSize() {
        return comparators.size();
    }
}
