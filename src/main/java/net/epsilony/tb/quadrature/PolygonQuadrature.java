/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import java.util.Iterator;
import net.epsilony.tb.solid.Facet;
import net.epsilony.tb.solid.Segment;

/**
 *
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public class PolygonQuadrature implements Quadrature<Segment2DQuadraturePoint> {

    Facet polygon;
    int degree = 2;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Facet getPolygon() {
        return polygon;
    }

    public void setPolygon(Facet polygon) {
        this.polygon = polygon;
    }

    @Override
    public Iterator<Segment2DQuadraturePoint> iterator() {
        return new InnerIterator();
    }

    @Override
    public int calcPointsNum() {
        int segmentSum = 0;
        for (Segment _seg : polygon) {
            segmentSum++;
        }
        return segmentSum * GaussLegendre.pointsNum(degree);
    }

    class InnerIterator implements Iterator<Segment2DQuadraturePoint> {

        public InnerIterator() {
            segIter = polygon.iterator();
            segmentQuadrature.setDegree(degree);
            if (segIter.hasNext()) {
                Segment segment = segIter.next();
                segmentQuadrature.setSegment(segment);
                iter = segmentQuadrature.iterator();
            }
        }
        Iterator<Segment2DQuadraturePoint> iter = null;
        Segment2DQuadrature segmentQuadrature = new Segment2DQuadrature();
        Iterator<? extends Segment> segIter;

        @Override
        public boolean hasNext() {
            return iter != null;
        }

        @Override
        public Segment2DQuadraturePoint next() {
            Segment2DQuadraturePoint res = iter.next();
            if (!iter.hasNext()) {
                if (segIter.hasNext()) {
                    segmentQuadrature.setSegment(segIter.next());
                    iter = segmentQuadrature.iterator();
                } else {
                    iter = null;
                }
            }
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
