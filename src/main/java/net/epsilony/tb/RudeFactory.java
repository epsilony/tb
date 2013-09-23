/* (c) Copyright by Man YUAN */
package net.epsilony.tb;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RudeFactory<T> implements Factory<T> {

    Class<? extends T> objectClass;
    private Constructor<? extends T> constructor;

    public Class<? extends T> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class<? extends T> objectClass) {
        _setObjectClass(objectClass);
    }

    private void _setObjectClass(Class<? extends T> objectClass) {
        this.objectClass = objectClass;
        try {
            constructor = objectClass.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new IllegalArgumentException("must be a class with null constructor!");
        }
    }

    public RudeFactory() {
    }

    public RudeFactory(Class<? extends T> objectClass) {
        _setObjectClass(objectClass);
    }

    @Override
    public T produce() {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalArgumentException("must be a class with null constructor!");
        }
    }
}
