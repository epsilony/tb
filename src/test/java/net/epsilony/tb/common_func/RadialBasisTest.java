/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.tb.common_func;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class RadialBasisTest {

    public RadialBasisTest() {
    }

    static class TestData {

        String coreName;
        double infRad;
        double[][] dists;
        double[][] exps;
        int diffOrder;
        int dim;
        double[][] rawPts;
    }

    TestData getDataFromJsonFile() {
        String fileName = "radial_basis.json";
        Reader reader = new BufferedReader(new InputStreamReader(RadialBasisTest.class.getResourceAsStream(fileName)));
        Gson gson = new Gson();
        TestData fromJson = gson.fromJson(reader, TestData.class);
        return fromJson;
    }

    @Test
    public void testByWendland_3_2() {
        TestData testData = getDataFromJsonFile();
        if (!testData.coreName.equalsIgnoreCase("wendland_3_2")) {
            throw new IllegalStateException();
        }
        if (testData.exps.length != testData.dists.length || testData.exps.length <= 0) {
            throw new IllegalStateException();
        }
        RadialBasis rb = new RadialBasis(new Wendland(4));
        rb.setDiffOrder(testData.diffOrder);
        rb.setDimension(testData.dim);

        for (int i = 0; i < testData.exps.length; i++) {
            double[] dists = testData.dists[i];
            double[] exp = testData.exps[i];
            double[] act = rb.values(dists, testData.infRad, null);
            assertArrayEquals(exp, act, 1e-12);
        }

    }
}
