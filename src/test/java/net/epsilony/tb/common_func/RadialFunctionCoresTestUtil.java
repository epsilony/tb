/* (c) Copyright by Man YUAN */
package net.epsilony.tb.common_func;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
@Ignore
public class RadialFunctionCoresTestUtil {

    public static class SampleData {

        double[] distanceSamples;
        double[][] resultsByDistance;
        double[] distanceSquareSamples;
        double[][] resultsByDistanceSquare;
    }
    String cmd;
    SampleData data;
    double errorLimit = 1e-7;
    int maxDiffOrder;
    RadialFunctionCore core;
    private static final Logger logger = LoggerFactory.getLogger(RadialFunctionCoresTestUtil.class);
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            super.starting(description);
            logger.info("start {}  {}", description.getMethodName(), core);
        }
    };

    public void setCore(RadialFunctionCore core) {
        this.core = core;

    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void genSampleData() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(cmd);
        Reader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        Gson gson = new Gson();
        data = gson.fromJson(reader, SampleData.class);
        int b = p.waitFor();
        if(b!=0){
            throw new IllegalStateException("cmd does not exit normally");
        }
        if (data == null || data.distanceSamples.length <= 0) {
            throw new IllegalStateException();
        }
    }

    public void test() throws IOException, InterruptedException {
        genSampleData();
        logger.info("test {}", core);
        logger.info("sample size {}", data.distanceSamples.length);
        testByDistance();
        testByDistanceSquare();
    }

    private void testByDistance() {
        logger.info("testByDistance");
        for (int diffOrder = 0; diffOrder <= maxDiffOrder; diffOrder++) {
            logger.info("diff order: {}", diffOrder);
            core.setDiffOrder(diffOrder);
            for (int i = 0; i < data.distanceSamples.length; i++) {
                double sample = data.distanceSamples[i];
                double[] exp = data.resultsByDistance[i];
                double[] act = core.valuesByDistance(sample, null);
                assertEquals(diffOrder + 1, act.length);
                double[] exp_trunk = Arrays.copyOf(exp, diffOrder + 1);
                assertArrayEquals(exp_trunk, act, errorLimit);
            }
        }
    }

    private void testByDistanceSquare() {
        logger.info("testByDistanceSquare");
        int diffOrder = 1;
        core.setDiffOrder(diffOrder);
        logger.info("diff order: {}", diffOrder);
        for (int i = 0; i < data.distanceSquareSamples.length; i++) {
            double sample = data.distanceSquareSamples[i];
            double[] exp = data.resultsByDistanceSquare[i];
            double[] act = core.valuesByDistanceSquare(sample, null);
            assertArrayEquals(exp, act, errorLimit);
        }

    }
}
