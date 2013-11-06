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

import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;
import net.epsilony.tb.analysis.Math2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ArcSegment2DTest {

    public ArcSegment2DTest() {
    }

    @Test
    public void testCalcCenter() {
        double radius = 3;
        double xTrans = -3.1;
        double yTrans = 2.2;
        ArcSegment2D arc = new ArcSegment2D();
        arc.setRadius(radius);
        arc.setStart(new Node(radius * cos(PI / 6) + xTrans, radius * sin(PI / 6) + yTrans));
        arc.setSucc(new Line(new Node(
                radius * cos(PI / 3) + xTrans,
                radius * sin(PI / 3) + yTrans)));
        double[] center = arc.calcCenter(null);
        double[] exp = new double[]{xTrans, yTrans};
        assertArrayEquals(exp, center, 1e-14);

        arc.setCenterOnChordLeft(false);
        center = arc.calcCenter(null);
        exp = new double[]{xTrans + 2 * radius * cos(PI / 12) * cos(PI / 4),
            yTrans + 2 * radius * cos(PI / 12) * sin(PI / 4)};
        assertArrayEquals(exp, center, 1e-14);
    }

    @Test
    public void testValues() {
        double radius = 3;
        double xTrans = -3.1;
        double yTrans = 2.2;
        for (boolean onChordLeft : new boolean[]{true, false}) {
            double startAngle = PI / 6;
            double endAngle = PI / 3;
            if (!onChordLeft) {
                startAngle = PI / 3;
                endAngle = PI / 6;
            }
            ArcSegment2D arc = new ArcSegment2D();
            arc.setCenterOnChordLeft(onChordLeft);
            arc.setRadius(radius);
            arc.setStart(new Node(radius * cos(startAngle) + xTrans, radius * sin(startAngle) + yTrans));
            arc.setSucc(new Line(new Node(
                    radius * cos(endAngle) + xTrans,
                    radius * sin(endAngle) + yTrans)));
            double[] samples = new double[]{0, 1, 0.5, 0.35};
            double[][] exps = new double[][]{
                {arc.getStart().getCoord()[0], arc.getStart().getCoord()[1]},
                {arc.getEnd().getCoord()[0], arc.getEnd().getCoord()[1]},
                {radius * cos((startAngle + endAngle) / 2) + xTrans,
                    radius * sin((startAngle + endAngle) / 2) + yTrans},
                {radius * cos(startAngle * (1 - 0.35) + endAngle * 0.35) + xTrans,
                    radius * sin(startAngle * (1 - 0.35) + endAngle * 0.35) + yTrans}
            };

            for (int i = 0; i < samples.length; i++) {
                double t = samples[i];
                double[] exp = exps[i];
                double[] act = arc.values(t, null);
                assertArrayEquals(exp, act, 1e-14);
            }
        }
    }

    @Test
    public void testValueDiff() {
        double radius = 3;
        double xTrans = -3.1;
        double yTrans = 2.2;
        for (boolean onChordLeft : new boolean[]{true, false}) {
            double startAngle = PI / 6;
            double endAngle = PI / 3;
            if (!onChordLeft) {
                startAngle = PI / 3;
                endAngle = PI / 6;
            }
            ArcSegment2D arc = new ArcSegment2D();
            arc.setCenterOnChordLeft(false);
            arc.setRadius(radius);
            arc.setStart(new Node(radius * cos(startAngle) + xTrans, radius * sin(startAngle) + yTrans));
            arc.setSucc(new Line(new Node(
                    radius * cos(endAngle) + xTrans,
                    radius * sin(endAngle) + yTrans)));
            arc.setCenterOnChordLeft(onChordLeft);
            arc.setDiffOrder(1);
            double sample = 0.33;
            double angle = startAngle * (1 - sample) + endAngle * sample;
            double[] exp = new double[]{
                radius * cos(angle) + xTrans,
                radius * sin(angle) + yTrans,
                -radius * sin(angle) * (endAngle - startAngle),
                radius * cos(angle) * (endAngle - startAngle)
            };
            double[] act = arc.values(sample, null);
            assertArrayEquals(act, exp, 1e-14);
        }
    }

    @Test
    public void testDistanceTo() {
        double radius = 3;
        double xTrans = -3.1;
        double yTrans = 2.2;
        ArcSegment2D arc = new ArcSegment2D();
        arc.setRadius(radius);
        arc.setStart(new Node(radius * cos(PI / 6) + xTrans, radius * sin(PI / 6) + yTrans));
        arc.setSucc(new Line(new Node(
                radius * cos(PI / 3) + xTrans,
                radius * sin(PI / 3) + yTrans)));

        double sampleRad = 2;
        double sampleAmpAngles = PI / 6 * 1.32;
        double exp = radius - sampleRad;
        double x = sampleRad * cos(sampleAmpAngles) + xTrans;
        double y = sampleRad * sin(sampleAmpAngles) + yTrans;
        double act = arc.distanceTo(x, y);
        assertEquals(exp, act, 1e-14);

        sampleAmpAngles = PI / 6 * 0.9;
        x = sampleRad * cos(sampleAmpAngles) + xTrans;
        y = sampleRad * sin(sampleAmpAngles) + yTrans;
        act = arc.distanceTo(x, y);
        exp = Math2D.distance(x, y, arc.getStart().getCoord()[0], arc.getStart().getCoord()[1]);
        assertEquals(exp, act, 1e-14);
    }

    @Test
    public void testBisection() {
        double radius = 3;
        double xTrans = -3.1;
        double yTrans = 2.2;

        for (boolean centerOnChordLeft : new boolean[]{true, false}) {
            double startAmpAngle = PI / 6;
            double endAmpAngle = PI * 2 / 3;
            if (!centerOnChordLeft) {
                double t = startAmpAngle;
                startAmpAngle = endAmpAngle;
                endAmpAngle = t;
            }
            ArcSegment2D arc = new ArcSegment2D();
            arc.setCenterOnChordLeft(centerOnChordLeft);
            arc.setRadius(radius);
            arc.setStart(new Node(radius * cos(startAmpAngle) + xTrans, radius * sin(startAmpAngle) + yTrans));
            arc.setSucc(new Line(new Node(
                    radius * cos(endAmpAngle) + xTrans,
                    radius * sin(endAmpAngle) + yTrans)));
            Segment rawTail = arc.getSucc();
            arc.bisect();
            ArcSegment2D newSucc = (ArcSegment2D) arc.getSucc();

            assertTrue(arc.getSucc() == newSucc);
            assertTrue(newSucc.getPred() == arc);
            assertTrue(rawTail == newSucc.getSucc());
            assertTrue(rawTail.getPred() == newSucc);

            double[] arcCenter = arc.calcCenter(null);
            double[] expCenter = new double[]{xTrans, yTrans};
            assertArrayEquals(expCenter, arcCenter, 1e-14);

            arcCenter = newSucc.calcCenter(null);
            assertArrayEquals(expCenter, arcCenter, 1e-14);

            double arcCenterAngle = arc.calcCenterAngle();
            double expArcCenterAngle = (startAmpAngle + endAmpAngle) / 2 - startAmpAngle;
            assertEquals(expArcCenterAngle, arcCenterAngle, 1e-14);

            double newSuccCenterAngle = newSucc.calcCenterAngle();
            assertEquals(expArcCenterAngle, newSuccCenterAngle, 1e-14);
        }
    }
}
