/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import net.epsilony.tb.implicit.TriangleContourCell;
import net.epsilony.tb.implicit.TriangleContourCellFactory;
import net.epsilony.tb.implicit.TriangleContourBuilder;
import net.epsilony.tb.implicit.MarchingTriangleContourBuilder;
import net.epsilony.tb.MiscellaneousUtils;
import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.LogicalMaximum;
import net.epsilony.tb.implicit.CircleLevelSet;
import net.epsilony.tb.implicit.ImplicitFunctionSolver;
import net.epsilony.tb.implicit.NewtonSolver;
import net.epsilony.tb.implicit.TrackContourBuilder;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.CommonFrame;
import net.epsilony.tb.ui.ModelDrawerAdapter;
import net.epsilony.tb.ui.ModelTransform;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TriangleContourBuilderDemo extends MouseAdapter {

    public static Logger logger = LoggerFactory.getLogger(TriangleContourBuilderDemo.class);
    //
    public static final int DRAG_OUTER = 1;
    public static final int DRAG_HOLE = 2;
    public static final int DRAG_NOTHING = 0;
    public TriangleContourBuilder polygonizer;
    public double holeRadius = 15;
    public double holeX = 44;
    public double holeY = 42;
    public double diskRadius = 30;
    public double diskX = 50;
    public double diskY = 50;
    public static final double HOLD_RADIUS_SUP = 40;
    double dragX, dragY;
    int dragStatus;
    SampleFunction sampleFunction = new SampleFunction();
    JCheckBox useNewton = new JCheckBox("Use Newton's method");
    JCheckBox useTrack = new JCheckBox("track");
    ImplicitFunctionSolver implicitFunctionSolver = new NewtonSolver();
    private CommonFrame frame;
    private TriangleContourBuilderDemoDrawer mainDrawer;

    public TriangleContourBuilderDemo() {
        useNewton.setSelected(true);
        useNewton.addActionListener(new UseNewtonListener());
        useTrack.setSelected(true);
        useTrack.addActionListener(new UseTrackListener());

        implicitFunctionSolver.setMaxEval(200);
        logger.info(implicitFunctionSolver.toString());
    }

    public class UseNewtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (useNewton.isSelected()) {
                polygonizer.setImplicitFunctionSolver(implicitFunctionSolver);
            } else {
                polygonizer.setImplicitFunctionSolver(null);
            }
            genContour();
            frame.getMainPanel().repaint();
        }
    }

    public void genContour() {
        try {
            logger.debug("hole radius: {}, disk radius: {}", holeRadius, diskRadius);
            polygonizer.genContour();
        } catch (Throwable e) {
            logger.error("", e);
        }
    }

    public class UseTrackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getMainPanel().getModelDrawers().remove(mainDrawer);
            genPolygonizer();
            mainDrawer = new TriangleContourBuilderDemoDrawer(polygonizer);
            frame.getMainPanel().addAndSetupModelDrawer(mainDrawer);
            genContour();
            frame.getMainPanel().repaint();
            useNewton.setEnabled(!useTrack.isSelected());
            if (useTrack.isSelected()) {
                useNewton.setSelected(true);
            }
        }
    }

    private void genPolygonizer() {
        TriangleContourCellFactory fatory = new TriangleContourCellFactory();
        TriangleContourCell[][] coverRectangle = fatory.coverRectangle(new Rectangle2D.Double(0, 0, 100, 100), 5);
        LinkedList<TriangleContourCell> cells = new LinkedList<>();
        MiscellaneousUtils.addToList(coverRectangle, cells);
        if (useTrack.isSelected()) {
            polygonizer = new TrackContourBuilder();
        } else {
            polygonizer = new MarchingTriangleContourBuilder();
        }
        polygonizer.setCells(cells);
        polygonizer.setLevelSetFunction(sampleFunction);

        polygonizer.setImplicitFunctionSolver(implicitFunctionSolver);
    }

    public class SampleFunction implements DifferentiableFunction<double[], double[]> {

        int diffOrder = 0;
        LogicalMaximum logMax = new LogicalMaximum();
        CircleLevelSet diskFun = new CircleLevelSet();
        CircleLevelSet holeFun = new CircleLevelSet();

        public SampleFunction() {
            logMax.setK(10, 0.25, false);
            logMax.setFunctions(diskFun, holeFun);
            diskFun.setCenterX(diskX);
            diskFun.setCenterY(diskY);
            diskFun.setRadius(diskRadius);
            diskFun.setConcrete(true);
            holeFun.setCenterX(holeX);
            holeFun.setCenterY(holeY);
            holeFun.setRadius(holeRadius);
            holeFun.setConcrete(false);
        }

        public void setDiskRadius(double r) {
            diskFun.setRadius(r);
        }

        public void setHoleRadius(double r) {
            holeFun.setRadius(r);
        }

        @Override
        public int getInputDimension() {
            return logMax.getInputDimension();
        }

        @Override
        public int getOutputDimension() {
            return logMax.getOutputDimension();
        }

        @Override
        public double[] value(double[] input, double[] output) {
            return logMax.value(input, output);
        }

        @Override
        public int getDiffOrder() {
            return logMax.getDiffOrder();
        }

        @Override
        public void setDiffOrder(int diffOrder) {
            logMax.setDiffOrder(diffOrder);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStatus = genDragStatus(e);
        if (dragStatus == DRAG_NOTHING) {
            return;
        }
        dragX = e.getX();
        dragY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int newDragStatus = genDragStatus(e);
        if (dragStatus != DRAG_NOTHING && newDragStatus == DRAG_NOTHING) {
            e.getComponent().repaint();
        }
        dragStatus = newDragStatus;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        dragStatus = genDragStatus(event);
        if (dragStatus == DRAG_NOTHING) {
            return;
        }

        BasicModelPanel panel = (BasicModelPanel) event.getComponent();
        ModelTransform modelToComponentTransform = panel.getModelToComponentTransform();
        double[] oriVector = new double[]{dragX, dragY};
        double[] dragedVector = new double[]{event.getX(), event.getY()};
        try {
            modelToComponentTransform.inverseTransform(oriVector, 0, oriVector, 0, 1);
            modelToComponentTransform.inverseTransform(dragedVector, 0, dragedVector, 0, 1);
        } catch (NoninvertibleTransformException ex) {
            logger.error("", ex);
        }

        double centerX, centerY;
        if (dragStatus == DRAG_HOLE) {
            centerX = holeX;
            centerY = holeY;
        } else {
            centerX = diskX;
            centerY = diskY;
        }

        double oriR = Math.sqrt(Math.pow(oriVector[0] - centerX, 2) + Math.pow(oriVector[1] - centerY, 2));
        double dragedR = Math.sqrt(Math.pow(dragedVector[0] - centerX, 2) + Math.pow(dragedVector[1] - centerY, 2));
        double radiusDelta = dragedR - oriR;
        if (dragStatus == DRAG_HOLE) {
            holeRadius += radiusDelta;
        } else {
            diskRadius += radiusDelta;
        }
        dragX = event.getX();
        dragY = event.getY();

        if (holeRadius < 0) {
            holeRadius = 0;
        }

        if (diskRadius < 0) {
            diskRadius = 0;
        }

        sampleFunction.setHoleRadius(holeRadius);
        sampleFunction.setDiskRadius(diskRadius);

        genContour();
        panel.repaint();
    }

    public static int genDragStatus(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK
                && (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0
                && (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == 0) {
            if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK) {
                return DRAG_OUTER;
            } else {
                return DRAG_HOLE;
            }
        }
        return DRAG_NOTHING;
    }

    public void genUI() {
        genPolygonizer();
        genContour();
        frame = new CommonFrame();
        mainDrawer = new TriangleContourBuilderDemoDrawer(polygonizer);
        frame.getMainPanel().addAndSetupModelDrawer(mainDrawer);
        frame.getMainPanel().setPreferredSize(new Dimension(800, 600));
        frame.getMainPanel().addMouseListener(this);
        frame.getMainPanel().addMouseMotionListener(this);
        frame.getMainPanel().addAndSetupModelDrawer(new DraggingDrawer());
        frame.getContentPane().add(new JLabel("Draw with right key or +SHILF"));
        frame.getContentPane().add(useTrack);
        frame.getContentPane().add(useNewton);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.pack();
        frame.setVisible(true);
    }

    class DraggingDrawer extends ModelDrawerAdapter {

        @Override
        public Rectangle2D getBoundsInModelSpace() {
            return null;
        }

        @Override
        public void drawModel(Graphics2D g2) {
            if (dragStatus == DRAG_NOTHING) {
                return;
            }
            double centerX, centerY;
            if (dragStatus == DRAG_OUTER) {
                centerX = diskX;
                centerY = diskY;
            } else {
                centerX = holeX;
                centerY = holeY;
            }
            double[] centers = new double[]{centerX, centerY};
            modelToComponentTransform.transform(centers, 0, centers, 0, 1);
            Path2D path = new Path2D.Double();
            path.moveTo(centers[0], centers[1]);
            path.lineTo(dragX, dragY);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.MAGENTA);
            g2.draw(path);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TriangleContourBuilderDemo demo = new TriangleContourBuilderDemo();
                demo.genUI();
            }
        });
    }
}
