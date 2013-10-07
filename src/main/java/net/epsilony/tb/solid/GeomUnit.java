/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

import java.io.Serializable;
import net.epsilony.tb.IntIdentity;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public interface GeomUnit extends IntIdentity, Serializable {

    GeomUnit getParent();

    void setParent(GeomUnit parent);
}