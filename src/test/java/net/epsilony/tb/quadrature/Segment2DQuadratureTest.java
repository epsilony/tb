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

import net.epsilony.tb.solid.ArcSegment2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.analysis.ArrvarFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Segment2DQuadratureTest {

    public Segment2DQuadratureTest() {
    }

    @Test
    public void testLength() {
        final double val = 1.3;
        ArrvarFunction func = new ArrvarFunction() {
            @Override
            public double value(double[] vec) {
                return val;
            }
        };

        Line seg = new Line(new Node(1, -1));
        seg.setSucc(new Line(new Node(-2, 3)));
        double exp = 5 * val;
        boolean getHere = false;
        for (int deg = 1; deg < GaussLegendre.MAXPOINTS * 2 - 1; deg++) {
            Segment2DQuadrature sq = new Segment2DQuadrature();
            sq.setDegree(deg);
            sq.setSegment(seg);
            double act = sq.quadrate(func);
            assertEquals(exp, act, 1e-12);
            getHere = true;
        }
        assertTrue(getHere);
    }

    @Test
    public void testLadderX() {
        ArrvarFunction func = new ArrvarFunction() {
            @Override
            public double value(double[] vec) {
                return vec[0];
            }
        };

        Line seg = new Line(new Node(1, 2));
        seg.setSucc(new Line(new Node(-2, 6)));
        double exp = -2.5;
        boolean getHere = false;
        for (int deg = 1; deg < GaussLegendre.MAXPOINTS * 2 - 1; deg++) {
            Segment2DQuadrature sq = new Segment2DQuadrature();
            sq.setDegree(deg);
            sq.setSegment(seg);
            double act = sq.quadrate(func);
            assertEquals(exp, act, 1e-12);
            getHere = true;
        }
        assertTrue(getHere);
    }

    @Test
    public void testLadderY() {
        ArrvarFunction func = new ArrvarFunction() {
            @Override
            public double value(double[] vec) {
                return vec[1];
            }
        };

        Line seg = new Line(new Node(1, 2));
        seg.setSucc(new Line(new Node(-2, 6)));
        double exp = 20;
        boolean getHere = false;
        for (int deg = 1; deg < GaussLegendre.MAXPOINTS * 2 - 1; deg++) {
            Segment2DQuadrature sq = new Segment2DQuadrature();
            sq.setDegree(deg);
            sq.setSegment(seg);
            double act = sq.quadrate(func);
            assertEquals(exp, act, 1e-12);
            getHere = true;
        }
        assertTrue(getHere);
    }

    @Test
    public void testArcLength() {
        double startAngle = Math.PI * 0.33;
        double endAngle = Math.PI * 0.55;
        double xTrans = 13.1;
        double yTrans = -7.2;
        double radius = 33;
        double exp = Math.PI * (0.55 - 0.33) * radius;
        ArcSegment2D arc = new ArcSegment2D();
        arc.setStart(new Node(xTrans + radius * Math.cos(startAngle), yTrans + radius * Math.sin(startAngle)));
        arc.setRadius(radius);
        arc.setSucc(new Line(new Node(xTrans + radius * Math.cos(endAngle), yTrans + radius * Math.sin(endAngle))));
        Segment2DQuadrature sq = new Segment2DQuadrature();
        sq.setDegree(1);
        boolean beenHere = false;
        for (int deg = 3; deg < GaussLegendre.MAXPOINTS * 2 - 1; deg++) {
            sq.setDegree(deg);
            sq.setSegment(arc);
            double act = sq.quadrate(new ArrvarFunction() {
                @Override
                public double value(double[] vec) {
                    return 1;
                }
            });
            assertEquals(exp, act, 1e-14);
            beenHere = true;
        }
        assertTrue(beenHere);
    }
}
