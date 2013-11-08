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

package net.epsilony.tb.synchron;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class SynchronizedIterator<T> {

    public static <T> SynchronizedIterator<T> produce(Collection<? extends T> collection) {
        return new SynchronizedIterator<>(collection.iterator(), collection.size());
    }

    public SynchronizedIterator(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    public SynchronizedIterator(Iterator<? extends T> iterator, int estimatedSize) {
        this.iterator = iterator;
        this.estimatedSize = estimatedSize;
    }

    Iterator<? extends T> iterator;
    int estimatedSize = -1;
    int count = 0;

    public synchronized T nextItem() {
        if (iterator.hasNext()) {
            count++;
            return iterator.next();
        } else {
            return null;
        }
    }

    public synchronized int getEstimatedSize() {
        return estimatedSize;
    }

    public int getCount() {
        return count;
    }
}
