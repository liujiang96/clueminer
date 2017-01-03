/*
 * Copyright (C) 2011-2017 clueminer.org
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
package org.clueminer.chart.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import javax.swing.JPanel;
import org.clueminer.chart.api.Drawable;
import org.clueminer.chart.api.DrawingContext;

/**
 *
 * @author deric
 */
public class DrawablePanel extends JPanel {

    /**
     * Drawable that should be displayed.
     */
    private final Drawable drawable;

    /**
     * Defines whether this panel uses antialiasing.
     */
    private boolean antialiased;

    /**
     * Initializes a new instance with the specified {@code Drawable}.
     * Antialiasing is enabled by default.
     *
     * @param drawable {@code Drawable} to be displayed
     */
    public DrawablePanel(Drawable drawable) {
        this.drawable = drawable;
        setOpaque(false);
        antialiased = true;
    }

    /**
     * Returns the {@code Drawable} instance that is displayed by this panel.
     *
     * @return {@code Drawable} instance
     */
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isVisible()) {
            Graphics2D graphics = (Graphics2D) g;
            if (isAntialiased()) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            getDrawable().draw(new DrawingContext(graphics));
        }
        g.dispose();
    }

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        getDrawable().setBounds(bounds);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        getDrawable().setBounds(0.0, 0.0, width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dims = super.getPreferredSize();
        Dimension2D dimsPlot = getDrawable().getPreferredSize();
        dims.setSize(dimsPlot);
        return dims;
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getPreferredSize();
    }

    /**
     * Returns whether antialiasing is applied.
     *
     * @return {@code true} if the panel uses antialiasing, {@code false}
     *         otherwise.
     */
    public boolean isAntialiased() {
        return antialiased;
    }

    /**
     * Sets whether antialiasing should be applied.
     *
     * @param antialiased {@code true} if the panel should use antialiasing,
     *                    {@code false} otherwise.
     */
    public void setAntialiased(boolean antialiased) {
        this.antialiased = antialiased;
    }
}
