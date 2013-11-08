/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.epsilony.tb.implicit.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.epsilony.tb.analysis.DifferentiableFunction;
import net.epsilony.tb.analysis.LogicalMaximum;
import net.epsilony.tb.implicit.CircleLevelSet;
import net.epsilony.tb.implicit.MMAFunctionSolver;
import net.epsilony.tb.implicit.MarchingTriangle;
import net.epsilony.tb.implicit.SimpleBisectionSolver;
import net.epsilony.tb.implicit.SimpleGradientSolver;
import net.epsilony.tb.implicit.TrackContourBuilder;
import net.epsilony.tb.implicit.TriangleContourBuilder;
import net.epsilony.tb.implicit.TriangleContourCellFactory;
import net.epsilony.tb.implicit.ui.TrackContourSpecificationPanel;
import net.epsilony.tb.solid.winged.WingedCell;
import net.epsilony.tb.ui.BasicModelPanel;
import net.epsilony.tb.ui.ModelDrawerAdapter;
import net.epsilony.tb.ui.ModelTransform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public TriangleContourBuilder contourBuilder;
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
    private BasicModelPanel modelPanel;
    private TriangleContourBuilderDemoDrawer mainDrawer;
    String marchingLinear = "marching: linear";
    String marchingSimpleGradient = "marching: simple gradient";
    String marchingOnEdgeMMA = "marching: on edge MMA";
    String trackingSimpleGradient = "tracking: simple gradient";
    String marchingOnEdgeBisection = "marching: on edge bisection";
    String trackingMMA = "tracking: mma";
    String currentSelection = marchingLinear;
    List<WingedCell> cells;
    final Map<String, TriangleContourBuilder> builderMap = new HashMap<>();
    private JCheckBox showGradient = new JCheckBox("show gradient", true);
    private JCheckBox unitGradient = new JCheckBox("unit gradient", true);
    private JSlider unitGradientLength = new JSlider(1, 50);
    private JFormattedTextField gradientScale;
    private TrackContourSpecificationPanel specificationPanel;
    private JFrame frame;
    private JDialog dialog;

    public TriangleContourBuilderDemo() {
        genBuilderMap();
        genCells();
    }

    public void genContour() {
        try {
            logger.debug("hole radius: {}, disk radius: {}", holeRadius, diskRadius);
            contourBuilder.genContour();
        } catch (Throwable e) {
            logger.error("", e);
        }
    }

    private void genBuilderMap() {
        builderMap.clear();
        MarchingTriangle.LinearInterpolate linearInterpolate = new MarchingTriangle.LinearInterpolate();
        linearInterpolate.setLevelSetFunction(sampleFunction);
        builderMap.put(marchingLinear, linearInterpolate);

        SimpleGradientSolver simpleGradientSolver = new SimpleGradientSolver();
        simpleGradientSolver.setMaxEval(200);
        MarchingTriangle.FreeGradient freeGradient = new MarchingTriangle.FreeGradient();
        freeGradient.setLevelSetFunction(sampleFunction);
        freeGradient.setSolver(simpleGradientSolver);
        builderMap.put(marchingSimpleGradient, freeGradient);

        MarchingTriangle.OnEdge onEdgeMMA = new MarchingTriangle.OnEdge();
        MMAFunctionSolver solver = new MMAFunctionSolver(1);
        solver.setMaxEval(200);
        solver.setFunctionAbsoluteTolerence(1e-5);
        onEdgeMMA.setSolver(solver);
        onEdgeMMA.setLevelSetFunction(sampleFunction);
        builderMap.put(marchingOnEdgeMMA, onEdgeMMA);

        MarchingTriangle.OnEdge onEdgeBisection = new MarchingTriangle.OnEdge();
        onEdgeBisection.setSolver(new SimpleBisectionSolver());
        onEdgeBisection.getSolver().setMaxEval(200);
        onEdgeBisection.setLevelSetFunction(sampleFunction);
        builderMap.put(marchingOnEdgeBisection, onEdgeBisection);

        TrackContourBuilder trackSimpGrad = new TrackContourBuilder();
        SimpleGradientSolver simpGradient = new SimpleGradientSolver();
        simpGradient.setMaxEval(200);
        trackSimpGrad.setLevelSetFunction(sampleFunction);
        trackSimpGrad.setImplicitFunctionSolver(simpGradient);
        final PropertyChangeListener specificationChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                genContour();
                modelPanel.repaint();
            }
        };
        trackSimpGrad.getSpecification().addPropertyChangeListener(specificationChangeListener);
        builderMap.put(trackingSimpleGradient, trackSimpGrad);

        TrackContourBuilder trackMMA = new TrackContourBuilder();
        MMAFunctionSolver mmaSolver = new MMAFunctionSolver(2);
        mmaSolver.setMaxEval(200);
        trackMMA.setLevelSetFunction(sampleFunction);
        mmaSolver.setFunctionAbsoluteTolerence(1e-5);
        trackMMA.setImplicitFunctionSolver(mmaSolver);
        trackMMA.getSpecification().addPropertyChangeListener(specificationChangeListener);
        builderMap.put(trackingMMA, trackMMA);
    }

    @SuppressWarnings("unchecked")
    private void genContourBuilder() {
        contourBuilder = builderMap.get(currentSelection);
        contourBuilder.setCells((List) cells);
        contourBuilder.setLevelSetFunction(sampleFunction);
    }

    private void genCells() {
        TriangleContourCellFactory fatory = new TriangleContourCellFactory();
        fatory.setRectangle(new Rectangle2D.Double(0, 0, 100, 100));
        fatory.setEdgeLength(5);
        cells = fatory.produce();
    }

    public class SampleFunction implements DifferentiableFunction {

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
        double[] oriVector = new double[] { dragX, dragY };
        double[] dragedVector = new double[] { event.getX(), event.getY() };
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

    public class LeftRadioListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionString = e.getActionCommand();
            if (!actionString.equals(currentSelection)) {
                currentSelection = actionString;
                genContourBuilder();
                genContour();
                modelPanel.getModelDrawers().remove(mainDrawer);
                mainDrawer = new TriangleContourBuilderDemoDrawer(contourBuilder);
                updateGradientDrawingSetting();
                modelPanel.addAndSetupModelDrawer(mainDrawer);
                modelPanel.repaint();
                logger.info("change to {}", contourBuilder);
                if (contourBuilder instanceof TrackContourBuilder) {
                    TrackContourBuilder tcb = (TrackContourBuilder) contourBuilder;
                    specificationPanel.setSpecification((tcb.getSpecification()));
                } else {
                    specificationPanel.setSpecification(null);
                }
            }
            if (contourBuilder instanceof TrackContourBuilder) {
                dialog.setVisible(true);
            }
        }
    }

    public void genUI() {
        genContourBuilder();
        genContour();

        modelPanel = new BasicModelPanel();
        mainDrawer = new TriangleContourBuilderDemoDrawer(contourBuilder);
        modelPanel.addAndSetupModelDrawer(mainDrawer);
        modelPanel.setPreferredSize(new Dimension(800, 600));
        modelPanel.addMouseListener(this);
        modelPanel.addMouseMotionListener(this);
        modelPanel.addAndSetupModelDrawer(new DraggingDrawer());

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(modelPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        frame.getContentPane().add(rightPanel, BorderLayout.LINE_END);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        ButtonGroup buttonGroup = new ButtonGroup();
        boolean first = true;
        LeftRadioListener buttonAction = new LeftRadioListener();
        List<String> methods = new ArrayList<>(builderMap.keySet());
        Collections.sort(methods);
        for (String buttonName : methods) {
            JRadioButton button = new JRadioButton(buttonName);
            button.setActionCommand(buttonName);
            if (first) {
                button.setSelected(true);
                first = false;
            }
            buttonGroup.add(button);
            button.addActionListener(buttonAction);
            rightPanel.add(button);
        }

        rightPanel.add(Box.createVerticalStrut(2));
        showGradient = new JCheckBox("show gradient", true);
        showGradient.setSelected(true);
        showGradient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGradientDrawingSetting();
                modelPanel.repaint();
            }
        });

        rightPanel.add(showGradient);
        unitGradient = new JCheckBox("unit gradient", true);
        unitGradient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGradientDrawingSetting();
                modelPanel.repaint();
            }
        });

        rightPanel.add(unitGradient);
        rightPanel.add(Box.createVerticalStrut(2));
        unitGradientLength = new JSlider(1, 50);
        rightPanel.add(unitGradientLength);
        unitGradientLength.setValue(mainDrawer.nodeDrawer.getUnitGradientLength());
        unitGradientLength.setEnabled(mainDrawer.nodeDrawer.isDrawGradient());
        unitGradientLength.setPaintLabels(true);
        unitGradientLength.setPaintTicks(true);
        unitGradientLength.setPaintTrack(true);
        unitGradientLength.setMajorTickSpacing(10);
        unitGradientLength.setMinorTickSpacing(1);
        unitGradientLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateGradientDrawingSetting();
                modelPanel.repaint();
            }
        });

        gradientScale = new JFormattedTextField(NumberFormat.getNumberInstance());
        gradientScale.setValue(mainDrawer.nodeDrawer.getGradientScale());
        gradientScale.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        rightPanel.add(gradientScale);
        gradientScale.setEnabled(!mainDrawer.nodeDrawer.isUnitGradient());
        gradientScale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGradientDrawingSetting();
                modelPanel.repaint();
            }
        });

        frame.pack();
        modelPanel.setZoomAllNeeded(true);
        frame.setVisible(true);

        specificationPanel = new TrackContourSpecificationPanel();
        dialog = new JDialog(frame, false);
        dialog.setSize(500, 400);
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.getContentPane().add(specificationPanel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void updateGradientDrawingSetting() {
        boolean b = showGradient.isSelected();
        mainDrawer.nodeDrawer.setDrawGradient(b);
        if (!b) {
            unitGradient.setEnabled(false);
            unitGradientLength.setEnabled(false);
            gradientScale.setEnabled(false);
        } else {
            unitGradient.setEnabled(true);
            if (unitGradient.isSelected()) {
                unitGradientLength.setEnabled(true);
                gradientScale.setEditable(false);
            } else {
                unitGradientLength.setEnabled(false);
                gradientScale.setEnabled(true);
            }
        }
        mainDrawer.nodeDrawer.setUnitGradient(unitGradient.isSelected());
        if (unitGradientLength.isEnabled()) {
            mainDrawer.nodeDrawer.setUnitGradientLength(unitGradientLength.getValue());
        }
        if (gradientScale.isEnabled()) {
            Number value = (Number) gradientScale.getValue();
            if (value.doubleValue() <= 0) {
                gradientScale.setValue(0.01);
            }
            mainDrawer.nodeDrawer.setUnitGradientLength(unitGradientLength.getValue());
            gradientScale.setText(value.toString());
            mainDrawer.nodeDrawer.setGradientScale(value.doubleValue());
        }

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
            double[] centers = new double[] { centerX, centerY };
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
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    logger.error("", ex);
                }
                TriangleContourBuilderDemo demo = new TriangleContourBuilderDemo();
                demo.genUI();
            }
        });
    }
}
