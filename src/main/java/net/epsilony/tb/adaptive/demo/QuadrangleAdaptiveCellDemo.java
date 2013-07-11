/* (c) Copyright by Man YUAN */
package net.epsilony.tb.adaptive.demo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.epsilony.tb.adaptive.AdaptiveCell;
import net.epsilony.tb.adaptive.QuadrangleAdaptiveCellFactory;
import net.epsilony.tb.TestTool;
import net.epsilony.tb.solid.Node;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class QuadrangleAdaptiveCellDemo extends AbstractAdaptiveCellDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuadrangleAdaptiveCellDemo().createDemoUI();
            }
        });
    }

    @Override
    protected List<AdaptiveCell<Node>> genCells() {
        double[] xs = TestTool.linSpace(0, 200, 10);
        double[] ys = TestTool.linSpace(100, 0, 5);
        QuadrangleAdaptiveCellFactory<Node> factory = new QuadrangleAdaptiveCellFactory<>();
        factory.setNodeClass(Node.class);
        factory.setXs(xs);
        factory.setYs(ys);
        return new ArrayList<AdaptiveCell<Node>>(factory.produce());

    }
}
