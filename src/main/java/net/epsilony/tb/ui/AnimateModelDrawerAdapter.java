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

package net.epsilony.tb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.Timer;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public abstract class AnimateModelDrawerAdapter extends ModelDrawerAdapter implements AnimateModelDrawer,
        ActionListener {

    public static final int DEFAULT_FRAME_GAP = 33;
    protected AnimationStatus status = AnimationStatus.INITIATE;
    private AnimationStatus statusToSwitch;
    protected int millisecondsBetweenFrame = DEFAULT_FRAME_GAP;
    private long[] frame = new long[2];
    protected Timer timer = new Timer(0, this);

    @Override
    public void switchStatus(AnimationStatus statusToSwitch) {
        this.statusToSwitch = statusToSwitch;
        if (status != statusToSwitch) {
            switch (statusToSwitch) {
            case APPEARING:
            case FADING:
                startTimer();
                break;
            default:
                break;
            }
        }
    }

    @Override
    public AnimationStatus getStatus() {
        return status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (statusToSwitch != status) {
            status = statusToSwitch;
            switch (status) {
            case INITIATE:
                frame[0] = 0;
                frame[1] = 0;
                timer.stop();
                return;
            case OVER:
            case FREEZING:
                timer.stop();
                return;
            case APPEARING:
            case FADING:
                break;
            }
        }
        switch (status) {
        case INITIATE:
        case OVER:
        case FREEZING:
            return;
        case APPEARING:
            frame[AnimationStatus.APPEARING.ordinal()]++;
            break;
        case FADING:
            frame[AnimationStatus.FADING.ordinal()]++;
            break;
        }

        callComponentToRepaint();
    }

    private void callComponentToRepaint() {
        Rectangle2D bounds = getBoundsInModelSpace();
        if (null == bounds) {
            component.repaint();
        } else {
            UIUtils.transformAndTidyRectangle(modelToComponentTransform, bounds, bounds);
            UIUtils.repaintRectangle2D(component, bounds);
        }
    }

    protected long getFrame(AnimationStatus status) {
        if (status != AnimationStatus.APPEARING && status != AnimationStatus.FADING) {
            throw new IllegalArgumentException();
        }
        return frame[status.ordinal()];
    }

    private void startTimer() {
        timer.setDelay(millisecondsBetweenFrame);
        timer.setRepeats(true);
        timer.start();
    }

    public int getMillisecondsBetweenFrame() {
        return millisecondsBetweenFrame;
    }

    public void setMillisecondsBetweenFrame(int millisecondsBetweenFrame) {
        this.millisecondsBetweenFrame = millisecondsBetweenFrame;
    }
}
