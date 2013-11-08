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

package net.epsilony.tb.nativelib.demo;

import java.io.InputStream;
import java.util.Arrays;
import net.epsilony.tb.nativelib.TriangleLibrary;
import net.epsilony.tb.nativelib.TriangleLibraryUtils;
import static net.epsilony.tb.nativelib.TriangleLibraryUtils.readPoly;
import org.bridj.Pointer;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleLibraryDemo {

    public static void main(String[] args) {
        InputStream in = TriangleLibraryDemo.class.getResourceAsStream("./A.poly");

        TriangleLibrary.TriangulateIO trin = readPoly(in, false);
        double[] points = trin.getPointList().getDoubles(trin.getNumberOfPoints() * 2);
        System.out.println("Arrays.toString(doubles) = " + Arrays.toString(points));
        TriangleLibrary.TriangulateIO triout = new TriangleLibrary.TriangulateIO();
        Pointer<TriangleLibrary.TriangulateIO> trinPt = Pointer.pointerTo(trin);
        Pointer<TriangleLibrary.TriangulateIO> trioutPt = Pointer.pointerTo(triout);
        TriangleLibrary.triangulate(Pointer.pointerToCString("pq"), trinPt, trioutPt, Pointer.NULL);
        TriangleLibraryUtils.freeOut(triout);
    }
}
