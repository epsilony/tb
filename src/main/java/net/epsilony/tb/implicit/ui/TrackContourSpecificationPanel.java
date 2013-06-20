/* (c) Copyright by Man YUAN */
package net.epsilony.tb.implicit.ui;

import javax.swing.JComponent;
import net.epsilony.tb.implicit.TrackContourSpecification;
import static java.lang.Math.*;

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class TrackContourSpecificationPanel extends javax.swing.JPanel {

    TrackContourSpecification specification;

    /**
     * Creates new form TrackContourSpecificationPanel
     */
    public TrackContourSpecificationPanel() {
        initComponents();
        updateEnables();
        updateValues();
    }

    public TrackContourSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TrackContourSpecification specification) {
        this.specification = specification;
        updateEnables();
        updateValues();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        closeHeadSearchAngle = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        expectSegmentCurve = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        maxSegmentCurve = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        headTolerence = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        tolerence = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        minLength = new javax.swing.JFormattedTextField(java.text.NumberFormat.getNumberInstance());
        jLabel7 = new javax.swing.JLabel();
        maxLength = new javax.swing.JFormattedTextField(java.text.NumberFormat.getNumberInstance());

        setPreferredSize(new java.awt.Dimension(570, 380));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridLayout(7, 2));

        jLabel1.setText("close head search angle (deg)");
        add(jLabel1);

        closeHeadSearchAngle.setMaximum(89);
        closeHeadSearchAngle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                closeHeadSearchAngleStateChanged(evt);
            }
        });
        add(closeHeadSearchAngle);

        jLabel2.setText("expect segment curve (deg)");
        add(jLabel2);

        expectSegmentCurve.setMaximum(89);
        expectSegmentCurve.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                expectSegmentCurveStateChanged(evt);
            }
        });
        add(expectSegmentCurve);

        jLabel3.setText("max segment curve (deg)");
        add(jLabel3);

        maxSegmentCurve.setMaximum(89);
        maxSegmentCurve.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maxSegmentCurveStateChanged(evt);
            }
        });
        add(maxSegmentCurve);

        jLabel4.setText("head perpendicular tolerence (deg)");
        add(jLabel4);

        headTolerence.setMaximum(89);
        headTolerence.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                headTolerenceStateChanged(evt);
            }
        });
        add(headTolerence);

        jLabel5.setText("perpendicular tolerence (deg)");
        add(jLabel5);

        tolerence.setMaximum(89);
        tolerence.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tolerenceStateChanged(evt);
            }
        });
        add(tolerence);

        jLabel6.setText("min segment length");
        add(jLabel6);

        minLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minLengthActionPerformed(evt);
            }
        });
        add(minLength);

        jLabel7.setText("max segment length");
        add(jLabel7);

        maxLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxLengthActionPerformed(evt);
            }
        });
        add(maxLength);
    }// </editor-fold>//GEN-END:initComponents

    private void minLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minLengthActionPerformed
        Number value = (Number) minLength.getValue();
        try {
            specification.setMinSegmentLength(value.doubleValue());
            minLength.setText(value.toString());
        } catch (IllegalArgumentException e) {
            minLength.setText(e.getMessage());
        }
        minLength.setText(value.toString());
    }//GEN-LAST:event_minLengthActionPerformed

    private void maxLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxLengthActionPerformed
        Number value = (Number) maxLength.getValue();
        try {
            specification.setMaxSegmentLength(value.doubleValue());
            maxLength.setText(value.toString());
        } catch (IllegalArgumentException e) {
            maxLength.setText(e.getMessage());
        }

    }//GEN-LAST:event_maxLengthActionPerformed

    private void closeHeadSearchAngleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_closeHeadSearchAngleStateChanged
        specification.setCloseHeadSearchAngle(toRadians(closeHeadSearchAngle.getValue()));
    }//GEN-LAST:event_closeHeadSearchAngleStateChanged

    private void expectSegmentCurveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_expectSegmentCurveStateChanged
        specification.setExpectSegmentCurve(toRadians(expectSegmentCurve.getValue()));
    }//GEN-LAST:event_expectSegmentCurveStateChanged

    private void maxSegmentCurveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_maxSegmentCurveStateChanged
        specification.setMaxSegmentCurve(toRadians(maxSegmentCurve.getValue()));
    }//GEN-LAST:event_maxSegmentCurveStateChanged

    private void headTolerenceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_headTolerenceStateChanged
        specification.setHeadPerpendicularTolerence(toRadians(headTolerence.getValue()));
    }//GEN-LAST:event_headTolerenceStateChanged

    private void tolerenceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tolerenceStateChanged
        specification.setPerpendicularTolerence(toRadians(tolerence.getValue()));
    }//GEN-LAST:event_tolerenceStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider closeHeadSearchAngle;
    private javax.swing.JSlider expectSegmentCurve;
    private javax.swing.JSlider headTolerence;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JFormattedTextField maxLength;
    private javax.swing.JSlider maxSegmentCurve;
    private javax.swing.JFormattedTextField minLength;
    private javax.swing.JSlider tolerence;
    // End of variables declaration//GEN-END:variables

    private void updateEnables() {
        JComponent[] comps = new JComponent[]{
            closeHeadSearchAngle,
            expectSegmentCurve,
            headTolerence,
            tolerence,
            maxSegmentCurve,
            minLength, maxLength};
        for (JComponent c : comps) {
            c.setEnabled(specification != null);
        }
    }

    private void updateValues() {
        if (specification == null) {
            return;
        }
        minLength.setValue(specification.getMinSegmentLength());
        maxLength.setValue(specification.getMaxSegmentLength());
        maxSegmentCurve.setValue((int) round(toDegrees(specification.getMaxSegmentCurve())));
        closeHeadSearchAngle.setValue((int) round(toDegrees(specification.getCloseHeadSearchAngle())));
        expectSegmentCurve.setValue((int) round(toDegrees(specification.getExpectSegmentCurve())));
        headTolerence.setValue((int) round(toDegrees(specification.getHeadPerpendicularTolerence())));
        tolerence.setValue((int) round(toDegrees(specification.getPerpendicularTolerence())));
    }
}