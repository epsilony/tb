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

    Segment segment = null;
    int degree = -1;
    double[] points;
    double[] weights;
    double startParameter = 0;
    double endParameter = 1;

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Segment2DQuadrature() {
    }

    public void setDegree(int degree) {
        GaussLegendre.checkDegree(degree);
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
        Segment currentSegment = segment;
        int currentStartBase = 0;

        @Override
        public boolean hasNext() {
            return nextIdx < points.length;
        }

        @Override
        public Segment2DQuadraturePoint next() {
            double point = points[nextIdx];
            double t = (point + 1) / 2 * (endParameter - startParameter) + startParameter;
            double t2 = t - currentStartBase;
            while (t2 > 1) {
                currentSegment = currentSegment.getSucc();
                if (null == currentSegment) {
                    throw new IllegalStateException();
                }
                currentStartBase++;
                t2 = t - currentStartBase;
            }
            currentSegment.setDiffOrder(1);
            currentSegment.values(t2, coordAndDifferential);
            double dx = coordAndDifferential[2];
            double dy = coordAndDifferential[3];
            double ds = Math.sqrt(dx * dx + dy * dy);
            double weight = weights[nextIdx] / 2 * ds * (endParameter - startParameter);

            double x = coordAndDifferential[0];
            double y = coordAndDifferential[1];
            nextIdx++;
            Segment2DQuadraturePoint result = new Segment2DQuadraturePoint();
            result.coord = new double[]{x, y};
            result.weight = weight;
            result.outerNormal = new double[]{-dy / ds, dx / ds};
            result.segment = currentSegment;
            result.segmentParameter = t2;
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

    public double getStartParameter() {
        return startParameter;
    }

    public double getEndParameter() {
        return endParameter;
    }

    public void setStartEndParameter(double startParameter, double endParameter) {
        if (startParameter < 0) {
            throw new IllegalArgumentException("start parameter must be >= 0, not " + startParameter);
        }
        if (endParameter <= startParameter) {
            throw new IllegalArgumentException(String.format("end parameter must > start parameter (start:%d, end:%d)", startParameter, endParameter));
        }
        this.startParameter = startParameter;
        this.endParameter = endParameter;
    }
}
