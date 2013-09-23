/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive;

import net.epsilony.tb.Factory;
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
        setNodeFactory(new Factory<Node>() {
            @Override
            public Node produce() {
                try {
                    return TriangleAdaptiveCellFactory.this.nodeClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.error("ND does not hava a null constructor!", ex);
                    throw new IllegalStateException(ex);
                }
            }
        });
    }

    public TriangleAdaptiveCellFactory() {
        setCellFactory(new Factory<TriangleAdaptiveCell>() {
            @Override
            public TriangleAdaptiveCell produce() {
                return new TriangleAdaptiveCell();
            }
        });
        setEdgeFactory(new Factory<AdaptiveCellEdge>() {
            @Override
            public AdaptiveCellEdge produce() {
                return new AdaptiveCellEdge();
            }
        });
    }
}
