/* (c) Copyright by Man YUAN */
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