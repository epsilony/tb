/* (c) Copyright by Man YUAN */
package net.epsilony.tb.solid;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Ring implements GeomUnit {

    int id;
    Segment head;

    @Override
    public GeomUnit getParent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Segment getHead() {
        return head;
    }

    public void setHead(Segment head) {
        this.head = head;
    }
}
