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

package net.epsilony.tb.implicit.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Arrays;
import net.epsilony.tb.analysis.Math2D;
import net.epsilony.tb.implicit.ContourNode;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.ui.NodeDrawer;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ContourNodeDrawer extends NodeDrawer {

    public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 255, 160);
    public static Stroke DEFAULT_GRADIENT_STROKE = new BasicStroke(0.5f);
    public static int DEFAULT_UNIT_GRADIENT_LENGTH = 25;
    private boolean unitGradient = true;
    private int unitGradientLength = DEFAULT_UNIT_GRADIENT_LENGTH;
    private boolean drawGradient = true;
    private double gradientScale = 0.1;
    private Color gradientColor = DEFAULT_GRADIENT_COLOR;
    private Stroke gradientStroke = DEFAULT_GRADIENT_STROKE;

    public boolean isDrawGradient() {
        return drawGradient;
    }

    public void setDrawGradient(boolean drawGradient) {
        this.drawGradient = drawGradient;
    }

    @Override
    public void drawModel(Graphics2D g2) {
        super.drawModel(g2);
        Node node = getNode();
        if (drawGradient && node instanceof ContourNode) {
            drawGradient((ContourNode) node, g2);
        }
    }

    private void drawGradient(ContourNode node, Graphics2D g2) {
        double[] functionValue = node.getFunctionValue();
        if (functionValue == null || functionValue.length != 3) {
            return;
        }
        double[] coord = node.getCoord();
        double[] nodePosition = new double[2];
        modelToComponentTransform.transform(coord, 0, nodePosition, 0, 1);
        double[] gradient = Arrays.copyOfRange(functionValue, 1, 3);
        Shape line;
        if (unitGradient) {
            Math2D.normalize(gradient, gradient);
            Math2D.scale(gradient, unitGradientLength, gradient);
            line = new Line2D.Double(
                    nodePosition[0], nodePosition[1],
                    nodePosition[0] + gradient[0], nodePosition[1] - gradient[1]);

        } else {
            Math2D.scale(gradient, gradientScale, gradient);
            line = new Line2D.Double(coord[0], coord[1], coord[0] + gradient[0], coord[1] + gradient[1]);
            line = modelToComponentTransform.createTransformedShape(line);
        }

        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(gradientStroke);
        g2.setColor(gradientColor);
        g2.draw(line);
        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    public boolean isUnitGradient() {
        return unitGradient;
    }

    public void setUnitGradient(boolean unitGradient) {
        this.unitGradient = unitGradient;
    }

    public int getUnitGradientLength() {
        return unitGradientLength;
    }

    public void setUnitGradientLength(int unitGradientLength) {
        this.unitGradientLength = unitGradientLength;
    }

    public double getGradientScale() {
        return gradientScale;
    }

    public void setGradientScale(double gradientScale) {
        this.gradientScale = gradientScale;
    }

    public Color getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(Color gradientColor) {
        this.gradientColor = gradientColor;
    }

    public Stroke getGradientStroke() {
        return gradientStroke;
    }

    public void setGradientStroke(Stroke gradientStroke) {
        this.gradientStroke = gradientStroke;
    }
}
