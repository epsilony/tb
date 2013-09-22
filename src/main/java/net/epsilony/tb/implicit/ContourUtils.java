/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit;

import java.util.List;
import net.epsilony.tb.solid.Line;
import net.epsilony.tb.solid.Segment2DUtils;
import net.epsilony.tb.solid.SegmentIterator;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class ContourUtils {

    public static double getMinSegmentLength(List<Line> contourHeads) {
        double min = Double.POSITIVE_INFINITY;
        for (Line head : contourHeads) {
            SegmentIterator<Line> iter = new SegmentIterator<>(head);
            while (iter.hasNext()) {
                Line seg = iter.next();
                double segLen = Segment2DUtils.chordLength(seg);
                if (segLen < min) {
                    min = segLen;
                }
            }
        }
        return min;
    }
}
