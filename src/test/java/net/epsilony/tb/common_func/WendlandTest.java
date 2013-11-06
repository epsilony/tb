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
public class WendlandTest {

    public WendlandTest() {
    }

    @Test
    public void testWendland() throws IOException, InterruptedException {
        String[] functionKeys = new String[]{"wendland_3_1", "wendland_3_2", "wendland_3_3"};
        int[] cs = new int[]{2, 4, 6};
        for (int i = 0; i < cs.length; i++) {
            String fk = functionKeys[i];
            int c = cs[i];
            Wendland w = new Wendland(c);
            String cmd = "python3 " + WendlandTest.class.getResource("wendland.py").getPath() + " " + fk;
            RadialFunctionCoresTestUtil ru = new RadialFunctionCoresTestUtil();
            ru.setCore(w);
            ru.setCmd(cmd);
            ru.test();
        }
    }
}
