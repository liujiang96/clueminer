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
package org.clueminer.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.clueminer.attributes.BasicAttrRole;
import org.clueminer.attributes.TimePointAttribute;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.Timeseries;
import org.clueminer.dataset.plugin.TimeseriesDataset;
import org.clueminer.gui.msg.NotifyUtil;
import org.clueminer.io.importer.api.AttributeDraft;
import org.clueminer.processor.spi.Processor;
import org.clueminer.types.TimePoint;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Time series data importer
 *
 * @author deric
 * @param <E>
 */
@ServiceProvider(service = Processor.class)
public class TimeseriesProcessor<E extends Instance> extends AbstractProcessor<E> implements Processor<E> {

    private static final Logger LOGGER = Logger.getLogger(TimeseriesProcessor.class.getName());

    private static final Pattern NUMBER = Pattern.compile("^\\d+(\\.\\d+)?");
    private static final Pattern numberWithPrefix = Pattern.compile("([a-z_ ]+)(\\d+(\\.\\d+)?)(\\w+)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern numberSuffix = Pattern.compile("\\d+(\\.\\d+)?(\\w+)");

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TimeseriesProcessor.class, "TimeseriesProcessor.displayName");
    }

    @Override
    protected Dataset<E> createDataset(ArrayList<AttributeDraft> inputAttr) {
        return new TimeseriesDataset(container.getInstanceCount());
    }

    @Override
    protected Map<Integer, Integer> attributeMapping(ArrayList<AttributeDraft> inputAttr) {
        //set attributes
        Map<Integer, Integer> inputMap = new HashMap<>();

        TimePoint tp[] = new TimePointAttribute[inputAttr.size()];
        AttributeDraft attrd;
        Matcher nmatch;
        int parsed = 0;
        for (int i = 0; i < tp.length; i++) {
            attrd = inputAttr.get(i);
            String name = attrd.getName();
            String input = null;
            if (NUMBER.matcher(name).matches()) {
                //just number in the attribute name
                input = name;
            } else if ((nmatch = numberWithPrefix.matcher(name)).matches()) {
                //attibute like time1, time2
                input = nmatch.group(2);
            } else if ((nmatch = numberSuffix.matcher(name)).matches()) {
                input = nmatch.group(1);
            } else {
                //try to be smart, probably not a timeseries attribute
                attrd.setMeta(true);
                attrd.setRole(BasicAttrRole.META);
                NotifyUtil.warn("time attribute error", "failed to parse '" + name + "' as a number", true);
            }
            if (input != null) {
                try {
                    double pos = Double.valueOf(input);
                    tp[i] = new TimePointAttribute(i, (long) pos, pos);
                    inputMap.put(attrd.getIndex(), i);
                    parsed++;
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "failed to parse ''{0}'' as a number", name);
                    NotifyUtil.warn("time attribute error", "failed to parse '"
                            + name + "' as a number", true);
                }
            }
        }
        if (parsed < tp.length) {
            TimePoint copy[] = new TimePointAttribute[parsed];
            System.arraycopy(tp, 0, copy, 0, parsed);
            tp = copy;
        }

        ((Timeseries) dataset).setTimePoints(tp);

        return inputMap;
    }

}
