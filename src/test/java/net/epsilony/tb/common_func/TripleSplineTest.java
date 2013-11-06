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

package net.epsilony.tb.common_func;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TripleSplineTest {

    public TripleSplineTest() {
    }

    @Test
    public void testTripleSpline() throws IOException, InterruptedException {
        String path = TripleSplineTest.class.getResource("triple_spline.py").getPath();
        String[] cmds = new String[]{
            "python3 " + path + " test1",
            "python3 " + path
        };

        for (String cmd : cmds) {

            RadialFunctionCoresTestUtil ru = new RadialFunctionCoresTestUtil();
            ru.setCmd(cmd);
            ru.setCore(new TripleSpline());
            ru.test();
        }
    }
}
