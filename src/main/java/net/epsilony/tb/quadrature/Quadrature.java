/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

/**
 *
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public interface Quadrature<T extends QuadraturePoint> extends Iterable<T> {

    int calcPointsNum();
}
