/* (c) Copyright by Man YUAN */
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
