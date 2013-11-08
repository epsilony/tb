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
            throw new UnsupportedOperationException("Not supported yet."); // To
                                                                           // change
                                                                           // body
                                                                           // of
                                                                           // generated
                                                                           // methods,
                                                                           // choose
                                                                           // Tools
                                                                           // |
                                                                           // Templates.
        }
    }
}
