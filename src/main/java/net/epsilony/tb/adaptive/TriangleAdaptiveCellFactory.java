/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.RudeFactory;
import net.epsilony.tb.solid.Node;
import net.epsilony.tb.solid.winged.RectangleCoverTriangleCellsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleAdaptiveCellFactory extends RectangleCoverTriangleCellsFactory {

    public static Logger logger = LoggerFactory.getLogger(TriangleAdaptiveCellFactory.class);
    Class<? extends Node> nodeClass;

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
        setNodeFactory(new RudeFactory<>(this.nodeClass));
    }

    public TriangleAdaptiveCellFactory() {
        setCellFactory(new RudeFactory<>(TriangleAdaptiveCell.class));
        setEdgeFactory(new RudeFactory<>(AdaptiveCellEdge.class));
    }
}
