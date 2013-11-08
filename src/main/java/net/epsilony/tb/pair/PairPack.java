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

import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class PairPack<K, V> implements WithPair<K, V> {

    public K key;
    public V value;

    public PairPack(K value, V attach) {
        this.key = value;
        this.value = attach;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    public static <K, V> List<WithPair<K, V>> pack(List<? extends K> ks, List<? extends V> vs,
            List<WithPair<K, V>> result) {
        if (ks.size() != vs.size()) {
            throw new IllegalArgumentException("ks.size() and vs.size() is different (" + ks.size() + " and "
                    + vs.size());
        }

        result.clear();

        Iterator<? extends K> kIter = ks.iterator();
        for (V v : vs) {
            K k = kIter.next();
            result.add(new PairPack<>(k, v));
        }
        return result;
    }
}
