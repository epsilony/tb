/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.winged;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.linked.TDoubleLinkedList;
import gnu.trove.list.linked.TIntLinkedList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.Factory;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Segment;
import net.epsilony.tb.solid.SegmentIterator;
import static net.epsilony.tb.nativelib.TriangleLibrary.*;
import net.epsilony.tb.nativelib.TriangleLibraryUtils;
import net.epsilony.tb.solid.GeneralPolygon2D;
import org.bridj.Pointer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class PolygonTriangulatorFactory//
        <CELL extends WingedCell<CELL, EDGE, NODE>, //
        EDGE extends WingedEdge<CELL, EDGE, NODE>, //
        NODE extends Node> implements Factory<TriangleArrayContainers<CELL, NODE>> {

    GeneralTriangleCellFactory<CELL, EDGE, NODE> generalCellFactory = new GeneralTriangleCellFactory<>();
    GeneralPolygon2D<EDGE, NODE> polygon;
    private TriangulateIO triIn;
    private TriangulateIO triOut;
    double triangleArea = -1;
    double minAngleDegree = -1;
    boolean prohibitEdgeSteinerPoint = true;
    private ArrayList<CELL> triangleCells;
    private ArrayList<NODE> nodes;
    private List<NODE> spaceNodes;

    public double getTriangleArea() {
        return triangleArea;
    }

    public void setTriangleArea(double triangleArea) {
        this.triangleArea = triangleArea;
    }

    public double getMinAngleDegree() {
        return minAngleDegree;
    }

    public void setMinAngleDegree(double minAngleDegree) {
        this.minAngleDegree = minAngleDegree;
    }

    public boolean isProhibitEdgeSteinerPoint() {
        return prohibitEdgeSteinerPoint;
    }

    public void setProhibitEdgeSteinerPoint(boolean prohibitEdgeSteinerPoint) {
        this.prohibitEdgeSteinerPoint = prohibitEdgeSteinerPoint;
    }

    public void setPolygon(GeneralPolygon2D input) {
        this.polygon = input;
    }

    public Factory<? extends CELL> getCellFactory() {
        return generalCellFactory.getCellFactory();
    }

    public void setCellFactory(Factory<? extends CELL> cellFactory) {
        generalCellFactory.setCellFactory(cellFactory);
    }

    public Factory<? extends EDGE> getEdgeFactory() {
        return generalCellFactory.getEdgeFactory();
    }

    public void setEdgeFactory(Factory<? extends EDGE> edgeFactory) {
        generalCellFactory.setEdgeFactory(edgeFactory);
    }

    public Factory<? extends NODE> getNodeFactory() {
        return generalCellFactory.getNodeFactory();
    }

    public void setNodeFactory(Factory<? extends NODE> nodeFactory) {
        generalCellFactory.setNodeFactory(nodeFactory);
    }

    @Override
    public TriangleArrayContainers<CELL, NODE> produce() {
        triangulatePolygon();

        TriangleArrayContainers<CELL, NODE> result = new TriangleArrayContainers<>();
        result.triangles = triangleCells;
        result.nodes = nodes;
        result.spaceNodes = spaceNodes;
        return result;
    }

    private void triangulatePolygon() {
        triIn = new TriangulateIO();
        triOut = new TriangulateIO();
        genPointSegmentList();
        genHoleList();
        String sw = genSwitch();
        triangulate(Pointer.pointerToCString(sw), Pointer.pointerTo(triIn), Pointer.pointerTo(triOut), Pointer.NULL);
        extractNodes();
        extractTriangles();
        //TODO: move to dispose
        TriangleLibraryUtils.freeOut(triOut);
    }

    private void genPointSegmentList() {
        ArrayList<? extends Segment> chainsHeads = polygon.getChainsHeads();
        TDoubleList points = new TDoubleLinkedList();
        TIntList pointsMarks = new TIntLinkedList();
        TIntList segments = new TIntLinkedList();
        TIntList segmentsMarks = new TIntLinkedList();
        int numOfPoints = 0;
        int chainMark = 0;
        for (Segment head : chainsHeads) {
            SegmentIterator<Segment> iter = new SegmentIterator<>(head);
            double[] start = iter.next().getStart().getCoord();
            int startId = numOfPoints;
            points.add(start);
            pointsMarks.add(chainMark);
            numOfPoints++;

            double[] end;
            while (iter.hasNext()) {
                end = iter.next().getStart().getCoord();
                points.add(end);
                pointsMarks.add(chainMark);
                numOfPoints++;

                segments.add(numOfPoints - 2);
                segments.add(numOfPoints - 1);
                segmentsMarks.add(chainMark);
            }
            segments.add(numOfPoints - 1);
            segments.add(startId);
            segmentsMarks.add(chainMark);
            chainMark++;
        }
        triIn.setNumberOfPoints(numOfPoints);
        triIn.setPointList(Pointer.pointerToDoubles(points.toArray()));
        triIn.setPointMarkerList(Pointer.pointerToInts(pointsMarks.toArray()));

        triIn.setNumberOfSegments(segments.size() / 2);
        triIn.setSegmentList(Pointer.pointerToInts(segments.toArray()));
        triIn.setSegmentMarkerList(Pointer.pointerToInts(segmentsMarks.toArray()));
    }

    private void genHoleList() {
        List<double[]> pointsInHoles = polygon.getPointsInHoles();
        triIn.setNumberOfHoles(pointsInHoles.size());
        System.out.println("triIn.getNumberOfHole() = " + triIn.getNumberOfHoles());
        if (pointsInHoles.isEmpty()) {
            return;
        }
        double[] holes = new double[2 * pointsInHoles.size()];
        int i = 0;
        for (double[] h : pointsInHoles) {
            holes[i] = h[0];
            holes[i + 1] = h[1];
            i += 2;
        }
        triIn.setHoleList(Pointer.pointerToDoubles(holes));
    }

    private String genSwitch() {
        String result = "znpq";
        if (minAngleDegree > 0) {
            result += minAngleDegree;
        }
        if (prohibitEdgeSteinerPoint) {
            result += "Y";
        }
        if (triangleArea > 0) {
            result += "a" + triangleArea;
        }
        return result;
    }

    private void extractTriangles() {
        int numberOfTriangles = triOut.getNumberOfTriangles();

        int[] triangles = triOut.getTriangleList().getInts(3 * numberOfTriangles);
        int[] neighbours = triOut.getNeighborList().getInts(3 * numberOfTriangles);
        triangleCells = new ArrayList<>(numberOfTriangles);
        for (int i = 0; i < numberOfTriangles; i++) {
            CELL triangleCell = generalCellFactory.produce();
            for (int j = 0; j < 3; j++) {
                triangleCell.setVertex(j, nodes.get(triangles[i * 3 + j]));
            }
            if (Math2D.triangleArea(triangleCell) < 0) {
                NODE tn = triangleCell.getVertex(1);
                triangleCell.setVertex(1, triangleCell.getVertex(2));
                triangleCell.setVertex(2, tn);
            }
            triangleCells.add(triangleCell);
        }

        for (int i = 0; i < numberOfTriangles; i++) {
            CELL triangleCell = triangleCells.get(i);
            for (int j = 0; j < 3; j++) {
                int neighbourIndex = neighbours[i * 3 + j];
                if (neighbourIndex < 0) {
                    continue;
                }
                CELL nb = triangleCells.get(neighbourIndex);
                TriangleCellUtils.linkOppositesBySameVertes(triangleCell, nb);
            }
        }
    }

    private void extractNodes() {
        int numberOfPoints = triOut.getNumberOfPoints();
        double[] points = triOut.getPointList().getDoubles(2 * numberOfPoints);
        int[] pointsMarkers = triOut.getPointMarkerList().getInts(numberOfPoints);
        nodes = new ArrayList<>(numberOfPoints);
        generalCellFactory.setGenVertes(false);
        spaceNodes = new LinkedList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            NODE nd = generalCellFactory.nodeFactory.produce();
            nd.setCoord(new double[]{points[i * 2], points[i * 2 + 1]});
            nodes.add(nd);
            if (pointsMarkers[i] == 0) {
                spaceNodes.add(nd);
            }
        }
    }
}
