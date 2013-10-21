/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import java.util.Iterator;
import net.epsilony.tb.solid.Segment;
import net.epsilony.tb.analysis.ArrvarFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Segment2DQuadrature implements Iterable<Segment2DQuadraturePoint> {

    public static final int DEFAULT_DEGREE = 2;
    Segment segment = null;
    int degree = -1;
    double[] points;
    double[] weights;

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Segment2DQuadrature() {
        _setDegree(DEFAULT_DEGREE);
    }

    public void setDegree(int degree) {
        _setDegree(degree);
    }

    public int getNumOfPoints() {
        return weights.length;
    }

    private void _setDegree(int degree) {
        if (degree < 1) {
            throw new IllegalArgumentException("degree should be >= 1, not " + degree);
        }
        if (this.degree == degree) {
            return;
        }
        this.degree = degree;
        double[][] pws = GaussLegendre.pointsWeightsByDegree(degree);
        points = pws[0];
        weights = pws[1];
    }

    public int getDegree() {
        return degree;
    }

    @Override
    public Iterator<Segment2DQuadraturePoint> iterator() {
        if (degree < 0) {
            throw new IllegalStateException("quadrature degree must be set to a nonnegative one, not " + degree);
        }
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Segment2DQuadraturePoint> {

        int nextIdx = 0;
        private double[] coordAndDifferential = new double[4];

        @Override
        public boolean hasNext() {
            return nextIdx < points.length;
        }

        @Override
        public Segment2DQuadraturePoint next() {
            double point = points[nextIdx];
            double t = (point + 1) / 2;
            segment.setDiffOrder(1);
            segment.values(t, coordAndDifferential);
            double dx = coordAndDifferential[2];
            double dy = coordAndDifferential[3];
            double segLen = Math.sqrt(dx * dx + dy * dy);
            double weight = weights[nextIdx] / 2 * segLen;

            double x = coordAndDifferential[0];
            double y = coordAndDifferential[1];
            nextIdx++;
            Segment2DQuadraturePoint result = new Segment2DQuadraturePoint();
            result.coord = new double[]{x, y};
            result.weight = weight;
            result.outerNormal = new double[]{-dy / segLen, dx / segLen};
            result.segment = segment;
            result.segmentParameter = t;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public double quadrate(ArrvarFunction func) {
        double res = 0;
        for (QuadraturePoint qp : this) {
            res += func.value(qp.coord) * qp.weight;
        }
        return res;
    }
}
