/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class RawGeomUnit implements GeomUnit {

    protected int id;
    protected GeomUnit parent;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public GeomUnit getParent() {
        return parent;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setParent(GeomUnit parent) {
        this.parent = parent;
    }
}
