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

package net.epsilony.tb.pair;

import java.util.Comparator;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class WithPairComparator<K, V> implements Comparator<WithPair<K, V>> {

    Comparator<K> comparator;

    public WithPairComparator(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(WithPair<K, V> o1, WithPair<K, V> o2) {
        return comparator.compare(o1.getKey(), o2.getKey());
    }
}
