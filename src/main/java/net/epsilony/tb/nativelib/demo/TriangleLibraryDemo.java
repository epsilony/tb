/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.nativelib.demo;

import java.io.InputStream;
import java.util.Arrays;
import net.epsilony.tb.nativelib.TriangleLibrary;
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
    }
}
