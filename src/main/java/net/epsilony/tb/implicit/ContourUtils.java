/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.solid.Line2D;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ContourUtils {

    public static double getMinSegmentLength(List<Line2D> contourHeads) {
        double min = Double.POSITIVE_INFINITY;
        for (Line2D head : contourHeads) {
            SegmentIterator<Line2D> iter = new SegmentIterator<>(head);
            while (iter.hasNext()) {
                Line2D seg = iter.next();
                double segLen = Segment2DUtils.chordLength(seg);
                if (segLen < min) {
                    min = segLen;
                }
            }
        }
        return min;
    }
}
