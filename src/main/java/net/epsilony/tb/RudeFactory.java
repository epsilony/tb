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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RudeFactory<T> implements Factory<T> {

    Class<T> objectClass;
    private Constructor<T> constructor;

    public Class<? extends T> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class<T> objectClass) {
        _setObjectClass(objectClass);
    }

    private void _setObjectClass(Class<T> objectClass) {
        this.objectClass = objectClass;
        try {
            constructor = objectClass.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new IllegalArgumentException("must be a class with null constructor!");
        }
    }

    public RudeFactory() {
    }

    public RudeFactory(Class<T> objectClass) {
        _setObjectClass(objectClass);
    }

    @Override
    public T produce() {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
