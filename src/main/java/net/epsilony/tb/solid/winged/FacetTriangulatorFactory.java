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
import net.epsilony.tb.solid.Facet;
import org.bridj.Pointer;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class FacetTriangulatorFactory implements Factory<TriangleArrayContainers> {

    GeneralTriangleCellFactory generalCellFactory = new GeneralTriangleCellFactory();
    Facet facet;
    private TriangulateIO triIn;
    private TriangulateIO triOut;
    double triangleArea = -1;
    double minAngleDegree = -1;
    boolean prohibitEdgeSteinerPoint = true;
    private ArrayList<TriangleCell> triangleCells;
    private ArrayList<Node> nodes;
    private List<Node> spaceNodes;

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

    public void setFacet(Facet input) {
        this.facet = input;
    }

    public Factory<? extends WingedCell> getCellFactory() {
        return generalCellFactory.getCellFactory();
    }

    public void setCellFactory(Factory<? extends TriangleCell> cellFactory) {
        generalCellFactory.setCellFactory(cellFactory);
    }

    public Factory<? extends WingedEdge> getEdgeFactory() {
        return generalCellFactory.getEdgeFactory();
    }

    public void setEdgeFactory(Factory<? extends WingedEdge> edgeFactory) {
        generalCellFactory.setEdgeFactory(edgeFactory);
    }

    public Factory<? extends Node> getNodeFactory() {
        return generalCellFactory.getNodeFactory();
    }

    public void setNodeFactory(Factory<? extends Node> nodeFactory) {
        generalCellFactory.setNodeFactory(nodeFactory);
    }

    @Override
    public TriangleArrayContainers produce() {
        triangulatePolygon();

        TriangleArrayContainers result = new TriangleArrayContainers();
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
        // TODO: move to dispose
        TriangleLibraryUtils.freeOut(triOut);
    }

    private void genPointSegmentList() {
        List<? extends Segment> chainsHeads = facet.getRingsHeads();
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
        List<double[]> pointsInHoles = facet.getPointsInHoles();
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
            TriangleCell triangleCell = (TriangleCell) generalCellFactory.produce();
            for (int j = 0; j < 3; j++) {
                triangleCell.setVertex(j, nodes.get(triangles[i * 3 + j]));
            }
            if (Math2D.triangleArea(triangleCell) < 0) {
                Node tn = triangleCell.getVertex(1);
                triangleCell.setVertex(1, triangleCell.getVertex(2));
                triangleCell.setVertex(2, tn);
            }
            triangleCells.add(triangleCell);
        }

        for (int i = 0; i < numberOfTriangles; i++) {
            TriangleCell triangleCell = triangleCells.get(i);
            for (int j = 0; j < 3; j++) {
                int neighbourIndex = neighbours[i * 3 + j];
                if (neighbourIndex < 0) {
                    continue;
                }
                TriangleCell nb = triangleCells.get(neighbourIndex);
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
            Node nd = generalCellFactory.nodeFactory.produce();
            nd.setCoord(new double[] { points[i * 2], points[i * 2 + 1] });
            nodes.add(nd);
            if (pointsMarkers[i] == 0) {
                spaceNodes.add(nd);
            }
        }
    }
}
