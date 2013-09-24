/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import net.epsilony.tb.solid.Segment;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Segment2DQuadraturePoint extends QuadraturePoint {

    public Segment segment;
    public double segmentParameter;
    public double[] outerNormal;

    public Segment2DQuadraturePoint() {
    }

}
