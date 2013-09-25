/* (c) Copyright by Man YUAN */
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
        this.coord = new double[]{x, y};
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

    public static <ND extends Node> ND instanceByClass(ND nd) {
        try {
            return (ND) nd.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalStateException("ND doesn't have a null constructor!", ex);
        }
    }
}
