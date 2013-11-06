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

import java.util.ArrayList;
import net.epsilony.tb.RudeFactory;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Facet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class FacetTriangulatorFactoryTest {

    public FacetTriangulatorFactoryTest() {
    }
    double polygonSegmentLength = 5;

    @Test
    public void testSomeMethod() {
        FacetTriangulatorFactory factory = sampleSimpFactory();
        factory.setTriangleArea(25);
        Facet[] polygons = new Facet[]{sampleQuadrangleWithoutHole(), sampleFacePolygon()};
        int count = 0;
        for (Facet polygon : polygons) {
            System.out.println("test sample" + count++);
            factory.setFacet(polygon);
            TriangleArrayContainers triangleContainer = factory.produce();
            testTriangleAear(polygon, triangleContainer.triangles);
            RectangleCoverTriangleCellsFactoryTest.checkTriangleLink(triangleContainer.triangles);
        }
    }

    private void testTriangleAear(Facet polygon, ArrayList<TriangleCell> triangles) {
        double polygonArea = polygon.calcArea();
        double triangleAreaSum = 0;
        for (Triangle tri : triangles) {
            triangleAreaSum += Math2D.triangleArea(tri);
        }
        assertEquals(polygonArea, triangleAreaSum, 1e-6);
    }

    public FacetTriangulatorFactory sampleSimpFactory() {
        FacetTriangulatorFactory result = new FacetTriangulatorFactory();
        result.setCellFactory(new RudeFactory<>(TriangleCell.class));
        result.setEdgeFactory(new RudeFactory<>(RawWingedEdge.class));
        result.setNodeFactory(Node.factory());
        return result;
    }

    public Facet sampleQuadrangleWithoutHole() {
        double[][][] coords = new double[][][]{{
            {10, -10}, {100, 10}, {110, 90}, {5, 10}
        }
        };
        Facet polygon = Facet.byCoordChains(coords);
        polygon = polygon.fractionize(polygonSegmentLength);
        return polygon;
    }

    public Facet sampleFacePolygon() {
        //famouse face without inner segment
        double[][][] coords = new double[][][]{
            {//face
                {80, 0}, {100, 50}, {0, 100}, {-100, 50}, {-80, 0}, {-100, -50}, {0, -100}, {100, -50}
            },
            {//mouse hole
                {0, -90}, {-80, -50}, {0, -10}, {80, -50}
            },
            {//left eye hole 
                {-70, 50}, {-40, 55}, {-10, 55}, {-60, 30}
            },
            {//right eye hole
                {70, 50}, {60, 30}, {10, 55}, {40, 55}
            }
        };
        Facet polygon = Facet.byCoordChains(coords);
        polygon = polygon.fractionize(polygonSegmentLength);
        return polygon;
    }
}
