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

package net.epsilony.tb.matrix;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.UpperSPDDenseMatrix;
import no.uib.cipr.matrix.UpperSymmDenseMatrix;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ReverseCuthillMcKeeSolverTest {

    public ReverseCuthillMcKeeSolverTest() {
    }

    @Test
    public void testSolve() {
        double[][] sample = new double[][] { { 1, 0, 0, 4, 0, 6, 0, 0, 0, 0 }, { 0, 2, 3, 0, 5, 0, 7, 0, 0, 10 },
                { 0, 2, 3, 4, 5, 0, 0, 0, 0, 0 }, { 1, 0, 3, 4, 0, 6, 0, 0, 9, 0 }, { 0, 2, 3, 0, 5, 0, 7, 0, 0, 0 },
                { 1, 0, 0, 4, 0, 6, 7, 8, 0, 0 }, { 0, 2, 0, 0, 5, 6, 7, 8, 0, 0 }, { 0, 0, 0, 0, 0, 6, 7, 8, 0, 0 },
                { 0, 0, 0, 4, 0, 0, 0, 0, 9, 0 }, { 0, 2, 0, 0, 0, 0, 0, 0, 0, 10 } };
        DenseMatrix dense = new DenseMatrix(sample);
        // DenseMatrix dense=new DenseMatrix(10,10);
        for (int i = 0; i < 10; i++) {
            dense.add(i, i, 100);
        }
        DenseVector b = new DenseVector(dense.numRows());
        for (int i = 0; i < b.size(); i++) {
            b.set(i, 1);
        }
        ReverseCuthillMcKeeSolver rcm = new ReverseCuthillMcKeeSolver(dense, false);
        System.out.println(rcm.getOriginalBandWidth());
        System.out.println(rcm.getOptimizedBandWidth());

        DenseVector exp = (DenseVector) dense.solve(b, new DenseVector(b.size()));
        DenseVector act = rcm.solve(b);
        assertArrayEquals(exp.getData(), act.getData(), 1e-10);

        FlexCompColMatrix flexMat = new FlexCompColMatrix(dense);
        act = new ReverseCuthillMcKeeSolver(flexMat, false).solve(b);
        assertArrayEquals(exp.getData(), act.getData(), 1e-10);

        UpperSymmDenseMatrix uMat = new UpperSPDDenseMatrix(dense);
        uMat.solve(b, exp);
        act = new ReverseCuthillMcKeeSolver(uMat, true).solve(b);
        assertArrayEquals(exp.getData(), act.getData(), 1e-10);

        flexMat = new FlexCompColMatrix(uMat);
        act = new ReverseCuthillMcKeeSolver(flexMat, true).solve(b);
        assertArrayEquals(exp.getData(), act.getData(), 1e-10);
    }
}
