/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.analysis.UnivarArrayFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface Segment extends GeomUnit, UnivarArrayFunction {

    Node getStart();

    Segment getPred();

    Node getEnd();

    Segment getSucc();

    void setStart(Node start);

    void setPred(Segment pred);

    void setSucc(Segment succ);

    void bisect();
}
