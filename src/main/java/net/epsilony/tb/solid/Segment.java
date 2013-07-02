/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import net.epsilony.tb.IntIdentity;
import net.epsilony.tb.analysis.UnivarArrayFunction;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface Segment<PAS extends Segment<PAS,ND>, ND extends Node> extends IntIdentity, UnivarArrayFunction {

    ND getStart();

    PAS getPred();

    ND getEnd();

    PAS getSucc();

    void setStart(ND start);

    void setPred(PAS pred);

    void setSucc(PAS succ);
}
