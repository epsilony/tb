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
public class TriangleAdaptiveCellFactory<ND extends Node> extends RectangleCoverTriangleCellsFactory<AdaptiveCell<ND>, AdaptiveCellEdge<ND>, ND> {

    public static Logger logger = LoggerFactory.getLogger(TriangleAdaptiveCellFactory.class);
    Class<ND> nodeClass;

    public Class<ND> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<ND> nodeClass) {
        this.nodeClass = nodeClass;
        setNodeFactory(new Factory<ND>() {
            @Override
            public ND produce() {
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
        setCellFactory(new Factory<AdaptiveCell<ND>>() {
            @Override
            public AdaptiveCell<ND> produce() {
                return new TriangleAdaptiveCell<>();
            }
        });
        setEdgeFactory(new Factory<AdaptiveCellEdge<ND>>() {
            @Override
            public AdaptiveCellEdge<ND> produce() {
                return new AdaptiveCellEdge<>();
            }
        });
    }
}
