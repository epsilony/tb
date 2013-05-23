/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.IntIdentity;
import net.epsilony.tb.UnivarArrayFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface Segment extends IntIdentity, UnivarArrayFunction {

    Segment bisectionAndReturnNewSuccessor();

    Node getStart();

    double[] getStartCoord();

    Segment getPred();

    Node getEnd();

    double[] getEndCoord();

    Segment getSucc();

    void setStart(Node start);

    void setPred(Segment pred);

    void setSucc(Segment succ);
}
