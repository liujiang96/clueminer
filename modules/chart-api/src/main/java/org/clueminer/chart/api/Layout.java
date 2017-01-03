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
package org.clueminer.chart.api;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

/**
 * Interface that provides basic functions for arranging a layout.
 * Functionality includes the arrangement of components and returning the
 * preferred size of a specified container using this layout.
 */
public interface Layout extends Serializable {

    /**
     * Returns the amount of horizontal space between two layed out components.
     *
     * @return Space in pixels.
     */
    double getGapX();

    /**
     * Sets the amount of horizontal space between two layed out components.
     *
     * @param gapX Space in pixels.
     */
    void setGapX(double gapX);

    /**
     * Returns the amount of vertical space between two layed out components.
     *
     * @return Space in pixels.
     */
    double getGapY();

    /**
     * Sets the amount of horizontal space between two layed out components.
     *
     * @param gapY Space in pixels.
     */
    void setGapY(double gapY);

    /**
     * Arranges the components of the specified container according to this
     * layout.
     *
     * @param container Container to be laid out.
     */
    void layout(Container container);

    /**
     * Returns the preferred size of the specified container using this layout.
     *
     * @param container Container whose preferred size is to be returned.
     * @return Preferred extent of the specified container.
     */
    Dimension2D getPreferredSize(Container container);
}
