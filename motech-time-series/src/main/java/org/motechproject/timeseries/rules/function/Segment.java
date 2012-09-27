package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Segment {

    private int slotSize;

    public Segment(int slotSize) {
        this.slotSize = slotSize;
    }

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet result = new TimeSeriesSet();

        for (List<DataPoint> row : data.getData()) {
            for (List<DataPoint> resultRow : group(row, slotSize)) {
                result.addRow(resultRow);
            }
        }

        return result;
    }

    private List<List<DataPoint>> group(List<DataPoint> row, int slotSize) {
        List<List<DataPoint>> result = new ArrayList<>();
        List<DataPoint> currentSet = new ArrayList<>();
        int i = 0;

        for (DataPoint point : row) {
            if (i < slotSize) {
                currentSet.add(point);
                i++;
            } else {
                result.addAll(asList(currentSet));
                currentSet.removeAll(currentSet);
                i = 0;
            }
        }
        if (!currentSet.isEmpty()) {
            result.addAll(asList(currentSet));
        }
        return result;
    }
}
