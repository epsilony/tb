/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class NormalFunctionTest {

    public NormalFunctionTest() {
    }

    @Test
    public void testNormalFunction() throws IOException, InterruptedException {
        RadialFunctionCoresTestUtil testUtil = new RadialFunctionCoresTestUtil();
        NormalFunction core = new NormalFunction();
        core.setSigma(3);
        testUtil.setCore(core);
        testUtil.setCmd("python3 " + NormalFunctionTest.class.getResource("normal_function.py").getPath());
        testUtil.test();
    }
}
