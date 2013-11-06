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

package net.epsilony.tb.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class CoordinateMarker extends ModelDrawerAdapter {

    private static final Point2D ORIGIN = new Point2D.Float(0, 0);
    public static final boolean DEFAULT_VISIBLE = true;
    public static final float DEFAULT_AXIS_LENGTH = 20;
    public static final Color DEFAULT_X_COLOR = Color.RED;
    public static final Color DEFAULT_Y_COLOR = Color.GREEN;
    float axisLength = DEFAULT_AXIS_LENGTH;
    float strokeWidth = -1;
    Color xAxisColor = DEFAULT_X_COLOR;
    Color yAxisColor = DEFAULT_Y_COLOR;

    public CoordinateMarker(boolean visible) {
        this.visible = visible;
    }

    public CoordinateMarker() {
        this(DEFAULT_VISIBLE);
    }

    @Override
    public void drawModel(Graphics2D g2) {
        Point2D oriOnComponent = getModelToComponentTransform().transform(ORIGIN, null);
        GeneralPath path = new GeneralPath();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (strokeWidth > 0) {
            g2.setStroke(new BasicStroke(strokeWidth));
        }
        g2.setColor(xAxisColor);
        path.moveTo(oriOnComponent.getX(), oriOnComponent.getY());
        path.lineTo(oriOnComponent.getX() + axisLength, oriOnComponent.getY());
        g2.draw(path);
        g2.setColor(yAxisColor);
        path.reset();
        path.moveTo(oriOnComponent.getX(), oriOnComponent.getY());
        path.lineTo(oriOnComponent.getX(), oriOnComponent.getY() - axisLength);
        g2.draw(path);
    }

    public float getAxisLength() {
        return axisLength;
    }

    public void setAxisLength(float axisLength) {
        this.axisLength = axisLength;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Color getxAxisColor() {
        return xAxisColor;
    }

    public void setxAxisColor(Color xAxisColor) {
        this.xAxisColor = xAxisColor;
    }

    public Color getyAxisColor() {
        return yAxisColor;
    }

    public void setyAxisColor(Color yAxisColor) {
        this.yAxisColor = yAxisColor;
    }

    @Override
    public Rectangle2D getBoundsInModelSpace() {
        return null;
    }
}
