/*
 * (c) Copyright by Man YUAN
 */
package net.epsilony.tb.nativelib;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.bridj.Pointer;
import static net.epsilony.tb.nativelib.TriangleLibrary.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleLibraryUtils {

    public static TriangulateIO readPoly(InputStream in, boolean zeroStart) {
        TriangulateIO triangulateIO = new TriangulateIO();
        Scanner scanner = new Scanner(in);
        readPoints(scanner, triangulateIO, zeroStart);
        readSegments(scanner, triangulateIO, zeroStart);
        readHoles(scanner, triangulateIO, zeroStart);
        readRegions(scanner, triangulateIO, zeroStart);
        return triangulateIO;
    }

    private static void readPoints(Scanner scanner, TriangulateIO triangulateIO, boolean zeroStart) {
        int pointsNum = scanner.nextInt();
        int dimension = scanner.nextInt();
        if (dimension != 2) {
            throw new IllegalStateException();
        }
        int attributesNum = scanner.nextInt();
        boolean useBoundaryMarker = scanner.nextInt() != 0;
        triangulateIO.setNumberOfPoints(pointsNum);
        triangulateIO.setNumberOfPointAttributes(attributesNum);
        if (pointsNum <= 0) {
            return;
        }
        double[] points = new double[dimension * pointsNum];
        int[] pointsMarkers = useBoundaryMarker ? new int[pointsNum] : null;
        double[] pointsAttributes = new double[attributesNum * pointsNum];
        for (int i = 0; i < pointsNum; i++) {
            int number = scanner.nextInt();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();

            int index = 2 * (zeroStart ? number : number - 1);
            points[index] = x;
            points[index + 1] = y;

            index = attributesNum * (zeroStart ? number : number - 1);
            for (int j = index; j < index + attributesNum; j++) {
                pointsAttributes[j] = scanner.nextDouble();
            }

            if (useBoundaryMarker) {
                index = zeroStart ? number : number - 1;
                pointsMarkers[index] = scanner.nextInt();
            }
        }
        triangulateIO.setPointList(Pointer.pointerToDoubles(points));
        if (attributesNum > 0) {
            triangulateIO.setPointAttributeList(Pointer.pointerToDoubles(pointsAttributes));
        } else {
            triangulateIO.setPointAttributeList(Pointer.NULL);
        }
        if (useBoundaryMarker) {
            triangulateIO.setPointMarkerList(Pointer.pointerToInts(pointsMarkers));
        } else {
            triangulateIO.setPointMarkerList(Pointer.NULL);
        }
    }

    private static void readSegments(Scanner scanner, TriangulateIO triangulateIO, boolean zeroStart) {
        int segmentNum = scanner.nextInt();
        boolean useMarkers = scanner.nextInt() != 0;
        triangulateIO.setNumberOfSegments(segmentNum);
        if (segmentNum <= 0) {
            return;
        }
        int[] segments = new int[2 * segmentNum];
        int[] markers = useMarkers ? new int[segmentNum] : null;
        for (int i = 0; i < segmentNum; i++) {
            int number = scanner.nextInt();
            int point1 = scanner.nextInt();
            int point2 = scanner.nextInt();
            int index = 2 * (zeroStart ? number : number - 1);
            segments[index] = point1;
            segments[index + 1] = point2;
            if (useMarkers) {
                index = zeroStart ? number : number - 1;
                markers[index] = scanner.nextInt();
            }
        }
        triangulateIO.setSegmentList(Pointer.pointerToInts(segments));
        triangulateIO.setSegmentMarkerList(useMarkers ? Pointer.pointerToInts(markers) : Pointer.NULL);
    }

    private static void readHoles(Scanner scanner, TriangulateIO triangulateIO, boolean zeroStart) {
        int holeNum = scanner.nextInt();
        triangulateIO.setNumberOfHoles(holeNum);
        if (holeNum <= 0) {
            return;
        }
        double[] holes = new double[2 * holeNum];
        for (int i = 0; i < holeNum; i++) {
            int number = scanner.nextInt();
            int index = 2 * (zeroStart ? number : number - 1);
            holes[index] = scanner.nextDouble();
            holes[index + 1] = scanner.nextDouble();
        }
        triangulateIO.setHoleList(holeNum > 0 ? Pointer.pointerToDoubles(holes) : Pointer.NULL);
    }

    private static void readRegions(Scanner scanner, TriangulateIO triangulateIO, boolean zeroStart) {
        if (!scanner.hasNextInt()) {
            return;
        }
        int regionNum = scanner.nextInt();
        if (regionNum <= 0) {
            return;
        }
        triangulateIO.setNumberOfRegions(regionNum);
        double[] regions = new double[regionNum * 4];
        for (int i = 0; i < regionNum; i++) {
            int number = scanner.nextInt();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double attribute = scanner.nextDouble();
            double area = scanner.nextDouble();
            int index = 4 * (zeroStart ? number : number - 1);
            regions[index] = x;
            regions[index + 1] = y;
            regions[index + 2] = attribute;
            regions[index + 3] = area;
        }
        triangulateIO.setRegionList(Pointer.pointerToDoubles(regions));
    }
    private static final List<String> doNotFree = Arrays.asList(new String[]{"Number", "Hole", "Region"});

    public static void freeOut(TriangulateIO out) {
        Method[] methods = TriangulateIO.class.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (!name.startsWith("get")) {
                continue;
            }
            boolean fit = true;
            for (String doNot : doNotFree) {
                if (name.contains(doNot)) {
                    fit = false;
                    continue;
                }
            }
            if (!fit) {
                continue;
            }
            Pointer<?> pointer;
            try {
                pointer = (Pointer<?>) method.invoke(out);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new IllegalArgumentException();
            } 
            trifree(pointer);
        }
    }
}
