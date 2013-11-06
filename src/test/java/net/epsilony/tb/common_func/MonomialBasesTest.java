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

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class MonomialBasesTest {

    public MonomialBasesTest() {
    }

    public static class Data {

        double[][] samples;
        double[][][] exps;
        int dimension;

        @Override
        public String toString() {
            return "Data{" + "samples.length=" + samples.length + ", dimension=" + dimension + '}';
        }
    }

    public Data[] testDataFromJson() throws IOException {
        Reader reader = new InputStreamReader(MonomialBasesTest.class.getResourceAsStream("monomial_test_data.json"));
        reader = new BufferedReader(reader);
        Gson gson = new Gson();
        Data[] fromJson = gson.fromJson(reader, Data[].class);
        reader.close();
        return fromJson;
    }

    /**
     * Test of values method, of class MonomialBases.
     */
    @Test
    public void testValues() throws IOException {
        MonomialBases mb = new MonomialBases();
        mb.setDegree(3);
        mb.setDiffOrder(1);
        boolean tested = false;
        for (Data testData : testDataFromJson()) {
            tested = true;
            System.out.println(testData);
            mb.setDimension(testData.dimension);
            for (int i = 0; i < testData.samples.length; i++) {
                double[] vec = testData.samples[i];
                double[][] exps = testData.exps[i];
                double[][] acts = mb.values(vec, null);
                for (int j = 0; j < exps.length; j++) {
                    assertArrayEquals(exps[j], acts[j], 1e-14);
                }
            }
        }
        assertTrue(tested);
    }
}
