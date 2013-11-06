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
import java.util.Iterator;

/**
 *
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public class IntIdentityMap<K extends IntIdentity, V> implements Iterable<V> {

    public static final int NULL_INDEX_SUPREMUM = -1;
    ArrayList<V> values;

    public IntIdentityMap() {
        values = new ArrayList<>();
    }

    public IntIdentityMap(int initialCapacity) {
        values = new ArrayList<>(initialCapacity);
    }

    public V put(K key, V value) {
        final int keyId = key.getId();
        if (keyId >= size()) {
            throw new IllegalArgumentException("key.getId() is so big that it bursts the value array");
        }
        if (keyId > NULL_INDEX_SUPREMUM) {
            V old = values.get(keyId);
            values.set(keyId, value);
            return old;
        } else {
            key.setId(values.size());
            values.add(value);
            return null;
        }
    }

    public V get(K key) {
        final int keyId = key.getId();
        return getById(keyId);
    }

    public V getById(int keyId) {
        if (keyId > NULL_INDEX_SUPREMUM && keyId < values.size()) {
            return values.get(keyId);
        } else {
            return null;
        }
    }

    public void ensureCapacity(int minCapacity) {
        values.ensureCapacity(minCapacity);
    }

    public int size() {
        return values.size();
    }

    public void appendNullValues(int newSize) {
        ensureCapacity(newSize);
        for (int i = size(); i < newSize; i++) {
            values.add(null);
        }
    }

    public void clear() {
        values.clear();
    }

    @Override
    public Iterator<V> iterator() {
        return values.iterator();
    }
}
