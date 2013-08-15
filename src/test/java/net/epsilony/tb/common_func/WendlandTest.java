/* (c) Copyright by Man YUAN */
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