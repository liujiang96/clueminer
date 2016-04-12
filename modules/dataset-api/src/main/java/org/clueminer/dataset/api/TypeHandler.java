/*
 * Copyright (C) 2011-2016 clueminer.org
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
package org.clueminer.dataset.api;

import java.text.DecimalFormat;

/**
 * Interface for casting/converting objects into appropriate representation in Java.
 *
 * @author deric
 */
public interface TypeHandler<E extends Instance> {

    /**
     * Fast objects casting. Cast <code>value</code> as attribute <code>attr</code>
     *
     * @param value
     * @param attr
     * @param row
     */
    void handle(Object value, Attribute attr, E row, DecimalFormat df);
}