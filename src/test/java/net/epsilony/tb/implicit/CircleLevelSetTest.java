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

package net.epsilony.tb.implicit;

import java.util.Arrays;
import net.epsilony.tb.solid.ArcSegment2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class CircleLevelSetTest {

    public CircleLevelSetTest() {
    }

    @Test
    public void testToArcs() {
        double[] center = new double[] { -11.2, 2.3 };
        double radius = 4;
        int expArcNum = 8;
        CircleLevelSet circle = new CircleLevelSet(center[0], center[1], radius);
        ArcSegment2D head = circle.toArcs(4);
        ArcSegment2D seg = head;
        int arcNum = 0;
        while (true) {
            assertArrayEquals(center, seg.calcCenter(null), 1e-14);
            double actAngle = seg.calcStartAmplitudeAngle();
            double expAngle = Math.PI * 2 / expArcNum * arcNum;
            if (expAngle > Math.PI) {
                expAngle -= Math.PI * 2;
            }
            assertEquals(expAngle, actAngle, 1e-10);
            arcNum++;
            seg = (ArcSegment2D) seg.getSucc();
            if (seg == head) {
                break;
            }
        }
        assertEquals(expArcNum, arcNum);
    }

    @Test
    public void testFunction() {
        double centerX = -22.3;
        double centerY = 3.7;
        double radius = 4;
        CircleLevelSet circle = new CircleLevelSet();
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
        circle.setDiffOrder(1);

        double[][] testDatas = new double[][] { { centerX, centerY - radius, 0, 0, -2 * radius },
                { centerX + 2, centerY + 1, -11, 4, 2 } };

        for (double[] data : testDatas) {
            double[] coord = Arrays.copyOfRange(data, 0, 2);
            double[] exps = Arrays.copyOfRange(data, 2, 5);
            double[] acts = circle.value(coord, null);
            assertArrayEquals(exps, acts, 1e-6);
        }
    }
}
