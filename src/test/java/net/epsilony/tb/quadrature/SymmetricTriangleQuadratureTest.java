/* (c) Copyright by Man YUAN */
package net.epsilony.tb.quadrature;

import gnu.trove.list.array.TDoubleArrayList;
import java.util.Random;
import net.epsilony.tb.common_func.MonomialBasis2D;
import net.epsilony.tb.analysis.ArrvarFunction;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.triangle.ArrayTriangle;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:epsionyuan@gmail.com">Man YUAN</a>
 */
public class SymmetricTriangleQuadratureTest {

    public SymmetricTriangleQuadratureTest() {
    }

    @Test
    public void testAreaQuadrature() {
        System.out.println("quadrate for area");
        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 10;
        double y2 = 0.0;
        double x3 = 5;
        double y3 = 10;
        final double height = 0.8;
        double exp = height * x2 * y3 * 0.5;
        ArrvarFunction sampleFunc = new ArrvarFunction() {
            @Override
            public double value(double[] vec) {
                return height;
            }
        };
        SymmetricTriangleQuadrature ts = new SymmetricTriangleQuadrature();
        ArrayTriangle<Node> triangle = new ArrayTriangle<>();
        triangle.setVertex(0, new Node(x1, y1));
        triangle.setVertex(1, new Node(x2, y2));
        triangle.setVertex(2, new Node(x3, y3));
        ts.setTriangle(triangle);

        for (int degree = SymmetricTriangleQuadratureUtils.MIN_ALGEBRAIC_ACCURACY; degree <= SymmetricTriangleQuadratureUtils.MAX_ALGEBRAIC_ACCURACY; degree++) {
            ts.setDegree(degree);
            double act = ts.quadrate(sampleFunc);
            assertEquals(exp, act, 1e-12);
        }
    }

    @Test
    public void testPolygonQuadrature() {
        double x1 = 0.1;
        double y1 = 0.3;
        double x2 = 10.2;
        double y2 = 1.1;
        double x3 = 5.5;
        double y3 = 4.9;

        SymmetricTriangleQuadrature triQuad = new SymmetricTriangleQuadrature();
        ArrayTriangle<Node> triangle = new ArrayTriangle<>();
        triangle.setVertex(0, new Node(x1, y1));
        triangle.setVertex(1, new Node(x2, y2));
        triangle.setVertex(2, new Node(x3, y3));
        triQuad.setTriangle(triangle);

        for (int degree = 1; degree <= SymmetricTriangleQuadratureUtils.MAX_ALGEBRAIC_ACCURACY; degree++) {
            Random2DPolygon randPoly = new Random2DPolygon(degree);
            for (int i = degree; i <= SymmetricTriangleQuadratureUtils.MAX_ALGEBRAIC_ACCURACY; i++) {
                triQuad.setDegree(i);
                double act = triQuad.quadrate(randPoly);
                double exp = -integrateWithSimpson(randPoly, x1, y1, x2, y2)
                        + integrateWithSimpson(randPoly, x1, y1, x3, y3)
                        + integrateWithSimpson(randPoly, x3, y3, x2, y2);
                assertEquals(exp, act, Math.max(1e-6, 1e-6 * exp));
            }
        }
    }

    public double integrateWithSimpson(ArrvarFunction fun, double x1, double y1, double x2, double y2) {
        SimpsonIntegrator integrator = new SimpsonIntegrator();
        return integrator.integrate((int) 1e4, new IntegrateY(fun, x1, y1, x2, y2), x1, x2);
    }

    static class Random2DPolygon implements ArrvarFunction {

        @Override
        public double value(double[] vec) {
            TDoubleArrayList[] bs = basis.values(vec, null);
            double result = 0;
            for (int i = 0; i < bs[0].size(); i++) {
                result += bs[0].get(i) * pars[i];
            }
            return result;
        }
        double[] pars;
        MonomialBasis2D basis;

        public Random2DPolygon(int power) {
            basis = new MonomialBasis2D(power);
            pars = new double[basis.basisLength()];
            Random rand = new Random();
            for (int i = 0; i < pars.length; i++) {
                pars[i] = rand.nextDouble();
            }
        }
    }

    static class IntegrateY implements UnivariateFunction {

        @Override
        public double value(final double x) {
            double low = 0;
            double up = y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            UnivariateFunction f = new UnivariateFunction() {
                @Override
                public double value(double y) {
                    return func.value(new double[]{x, y});

                }
            };
            return simpIntegrator.integrate(10000, f, low, up);
        }
        final ArrvarFunction func;
        double x1, y1, x2, y2;
        SimpsonIntegrator simpIntegrator = new SimpsonIntegrator();

        public IntegrateY(ArrvarFunction func, double x1, double y1, double x2, double y2) {
            this.func = func;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}