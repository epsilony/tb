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

import gnu.trove.list.array.TIntArrayList;
import no.uib.cipr.matrix.DenseMatrix;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class GeneralNeiboursIteratorTest {

    public GeneralNeiboursIteratorTest() {
    }

    @Test
    public void testTheWholeIterator() {
        DenseMatrix denseMatrix = new DenseMatrix(new double[][]{
            {00, 00, 00, 11, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 11, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 11, 00, 11, 00, 11, 00, 11},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
            {00, 00, 00, 00, 00, 00, 00, 00, 00, 00},});
        GeneralNeiboursIterator iter = new GeneralNeiboursIterator(denseMatrix, 3, true);
        TIntArrayList acts = new TIntArrayList();
        while (iter.hasNext()) {
            acts.add(iter.next());
        }
        int[] exp = new int[]{0, 2, 5, 7, 9};
        assertArrayEquals(exp, acts.toArray());

        acts = new TIntArrayList();
        denseMatrix.set(3, 1, 11);
        iter = new GeneralNeiboursIterator(denseMatrix, 3, false);
        while (iter.hasNext()) {
            acts.add(iter.next());
        }
        exp = new int[]{1, 5, 7, 9};
        assertArrayEquals(exp, acts.toArray());

        acts = new TIntArrayList();
        denseMatrix.set(3, 1, 11);
        iter = new GeneralNeiboursIterator(denseMatrix, 3, true);
        while (iter.hasNext()) {
            acts.add(iter.next());
        }
        exp = new int[]{0, 2, 5, 7, 9};
        assertArrayEquals(exp, acts.toArray());
    }
}
