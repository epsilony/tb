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

import java.awt.Component;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Segment2DUtils;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class UIUtils {

    private static final double[] ZERO2D = new double[] { 0, 0 };

    public static Rectangle2D transformAndTidyRectangle(AffineTransform transform, Rectangle2D src, Rectangle2D dst) {
        if (null == dst) {
            dst = new Rectangle2D.Double();
        }
        double[] points = new double[] { src.getMinX(), src.getMinY(), src.getMaxX(), src.getMaxY() };
        transform.transform(points, 0, points, 0, 2);
        dst.setRect(points[0], points[1], points[2] - points[0], points[3] - points[1]);
        tidyRectangle2D(dst, dst);
        return dst;
    }

    public static void repaintRectangle2D(Component c, Rectangle2D rect) {
        tidyRectangle2D(rect, rect);
        c.repaint((int) Math.floor(rect.getX()), (int) Math.floor(rect.getY()), (int) Math.ceil(rect.getWidth()) + 1,
                (int) Math.ceil(rect.getHeight()) + 1);
    }

    public static Rectangle2D tidyRectangle2D(Rectangle2D src, Rectangle2D result) {
        if (null == result) {
            result = new Rectangle2D.Double();
        }
        double x = Math.min(src.getX(), src.getX() + src.getWidth());
        double y = Math.min(src.getY(), src.getY() + src.getHeight());
        double width = Math.abs(src.getWidth());
        double height = Math.abs(src.getHeight());
        result.setRect(x, y, width, height);
        return result;
    }

    public static double[] transformVector(AffineTransform transform, double[] vec, double[] result) {
        if (null == result) {
            result = new double[] { 2 };
        }
        transform.transform(vec, 0, result, 0, 1);
        double[] transformedOrigin = new double[2];
        transform.transform(ZERO2D, 0, transformedOrigin, 0, 1);
        result[0] = result[0] - transformedOrigin[0];
        result[1] = result[1] - transformedOrigin[1];
        return result;
    }

    public static List<Line> pathIteratorToSegment2DChains(PathIterator pathIterator) {
        List<Line> result = new LinkedList<>();
        Line start = null;
        Line current = start;
        double[] coords = new double[6];
        while (!pathIterator.isDone()) {
            int type = pathIterator.currentSegment(coords);
            switch (type) {
            case PathIterator.SEG_MOVETO:
                if (null != start) {
                    result.add(start);
                }
                start = new Line(new Node(coords[0], coords[1]));
                current = start;
                break;
            case PathIterator.SEG_LINETO:
                Line newSeg = new Line(new Node(coords[0], coords[1]));
                Segment2DUtils.link(current, newSeg);
                current = newSeg;
                break;
            case PathIterator.SEG_CLOSE:
                Segment2DUtils.link(current, start);
                result.add(start);
                start = null;
                current = null;
                break;
            default:
                throw new UnsupportedOperationException("Only Supports SEG_MOVETO, SEG_LINETO and SEG_CLOSE");
            }
            pathIterator.next();
        }
        if (start != null) {
            result.add(start);
        }
        return result;
    }
}
