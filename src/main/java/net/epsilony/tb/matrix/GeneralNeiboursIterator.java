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

import gnu.trove.iterator.TIntIterator;
import no.uib.cipr.matrix.Matrix;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class GeneralNeiboursIterator implements TIntIterator {

    Matrix mat;
    int nextNeibour = -1;
    int node;
    boolean upperSym;

    public GeneralNeiboursIterator(Matrix mat, int node, boolean upperSymmetric) {
        this.node = node;
        this.upperSym = upperSymmetric;
        this.mat = mat;
        if (!mat.isSquare()) {
            throw new IllegalArgumentException("mat should be square!");
        }
        findNext();
    }

    private void findNext() {
        do {
            nextNeibour++;
        } while (nextNeibour < mat.numColumns() && (nextNeibour == node || get(nextNeibour) == 0));
    }

    private double get(int index) {
        if (upperSym && index < node) {
            return mat.get(index, node);
        } else {
            return mat.get(node, index);
        }
    }

    @Override
    public int next() {
        int res = nextNeibour;
        findNext();
        return res;
    }

    @Override
    public boolean hasNext() {
        return nextNeibour < mat.numColumns();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
