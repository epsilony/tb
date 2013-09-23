/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AbstractLine extends RawSegment {

    public AbstractLine() {
    }

    public AbstractLine(Node node) {
        start = node;
    }
}
