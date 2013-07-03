/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid.triangle;

import java.util.ArrayList;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Polygon2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class PolygonTriangulatorFactoryTest {

    public PolygonTriangulatorFactoryTest() {
    }
    double polygonSegmentLength = 5;

    @Test
    public void testSomeMethod() {
        PolygonTriangulatorFactory<SimpTriangleCell<Node>, SimpTriangleEdge<Node>, Node> factory = sampleSimpFactory();
        factory.setTriangleArea(25);
        Polygon2D[] polygons = new Polygon2D[]{sampleQuadrangleWithoutHole(), sampleFacePolygon()};
        int count = 0;
        for (Polygon2D polygon : polygons) {
            System.out.println("test sample" + count++);
            factory.setPolygon(polygon);
            TriangleArrayContainers<SimpTriangleCell<Node>, Node> triangleContainer = factory.produce();
            testTriangleAear(polygon, triangleContainer.triangles);
            RectangleCoverTriangleCellsFactoryTest.checkTriangleLink(triangleContainer.triangles);
        }
    }

    private void testTriangleAear(Polygon2D polygon, ArrayList<SimpTriangleCell<Node>> triangles) {
        double polygonArea = polygon.calcArea();
        double triangleAreaSum = 0;
        for (Triangle tri : triangles) {
            triangleAreaSum += Math2D.triangleArea(tri);
        }
        assertEquals(polygonArea, triangleAreaSum, 1e-6);
    }

    public PolygonTriangulatorFactory<SimpTriangleCell<Node>, SimpTriangleEdge<Node>, Node> sampleSimpFactory() {
        PolygonTriangulatorFactory<SimpTriangleCell<Node>, SimpTriangleEdge<Node>, Node> result = new PolygonTriangulatorFactory<>();
        result.setCellFactory(SimpTriangleCell.factory());
        result.setEdgeFactory(SimpTriangleEdge.factory());
        result.setNodeFactory(Node.factory());
        return result;
    }

    public Polygon2D sampleQuadrangleWithoutHole() {
        double[][][] coords = new double[][][]{{
                {10, -10}, {100, 10}, {110, 90}, {5, 10}
            }
        };
        Polygon2D polygon = Polygon2D.byCoordChains(coords);
        polygon = polygon.fractionize(polygonSegmentLength);
        return polygon;
    }

    public Polygon2D sampleFacePolygon() {
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
        Polygon2D polygon = Polygon2D.byCoordChains(coords);
        polygon = polygon.fractionize(polygonSegmentLength);
        return polygon;
    }
}