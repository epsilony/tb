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

package net.epsilony.tb.ui.select;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

/**
 * 
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class RectangleMouseSelector extends MouseAdapter {

    private int rectangleSelectStartX, rectangleSelectStartY;
    LinkedList<RectangleSelectionListener> listeners = new LinkedList<>();
    private boolean inRectangleSelecting = false;

    @Override
    public void mousePressed(MouseEvent e) {
        if (isRectanglePickingStart(e)) {
            inRectangleSelecting = true;
            setRectangleStart(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isRectanglePickingStart(e)) {
            reportCadidateSelection(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isRectanglePicked(e)) {
            reportNewSelection(e);
            inRectangleSelecting = false;
        }
    }

    private boolean isRectanglePickingStart(MouseEvent e) {
        int mask = e.getModifiersEx();
        return ((mask & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                && ((mask & (MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK)) == 0);
    }

    private void setRectangleStart(MouseEvent e) {
        rectangleSelectStartX = e.getX();
        rectangleSelectStartY = e.getY();
    }

    private boolean isRectanglePicked(MouseEvent e) {
        return inRectangleSelecting && e.getButton() == MouseEvent.BUTTON1;
    }

    synchronized private void reportNewSelection(MouseEvent e) {
        RectangleSelectionEvent rectangleSelectionEvent = genRectangleSelectionEvent(e);
        for (RectangleSelectionListener l : listeners) {
            l.rectangleSelected(rectangleSelectionEvent);
        }
    }

    private RectangleSelectionEvent genRectangleSelectionEvent(MouseEvent e) {
        Rectangle2D rectangle = new Rectangle2D.Double(rectangleSelectStartX, rectangleSelectStartY,
                -rectangleSelectStartX + e.getX(), -rectangleSelectStartY + e.getY());
        RectangleSelectionEvent rectangleSelectionEvent = new RectangleSelectionEvent(this, rectangle,
                isKeepFormerSelections(e));
        return rectangleSelectionEvent;
    }

    private boolean isKeepFormerSelections(MouseEvent e) {
        return e.isShiftDown();
    }

    synchronized private void reportCadidateSelection(MouseEvent e) {
        RectangleSelectionEvent rectangleSelectionEvent = genRectangleSelectionEvent(e);
        for (RectangleSelectionListener l : listeners) {
            l.candidateRectangleSelected(rectangleSelectionEvent);
        }
    }

    synchronized public void addRectangleSelectionListener(RectangleSelectionListener l) {
        listeners.add(l);
    }

    synchronized public void removeRectangleSelectiongListener(RectangleSelectionListener l) {
        listeners.remove(l);
    }
}
