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

package net.epsilony.tb.solid;

import java.util.Arrays;

import net.epsilony.tb.Factory;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Node extends RawGeomUnit {

    protected double[] coord;

    public double[] getCoord() {
        return coord;
    }

    public void setCoord(double[] coord) {
        this.coord = coord;
    }

    public Node(double[] coord, boolean copy) {
        if (copy) {
            this.coord = Arrays.copyOf(coord, coord.length);
        } else {
            this.coord = coord;
        }
    }

    public Node(double[] coord) {
        this.coord = coord;
    }

    public Node(double x, double y) {
        this.coord = new double[] { x, y };
    }

    public Node() {
        this.coord = new double[2];
    }

    @Override
    public String toString() {
        return String.format("Node(%d)%s", id, Arrays.toString(coord));
    }

    public static Factory<Node> factory() {
        return new Factory<Node>() {
            @Override
            public Node produce() {
                return new Node();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <ND extends Node> ND instanceByClass(ND nd) {
        try {
            return (ND) nd.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalStateException("ND doesn't have a null constructor!", ex);
        }
    }
}
