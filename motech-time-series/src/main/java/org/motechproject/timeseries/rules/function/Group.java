package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class Group {


    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet result = new TimeSeriesSet();

        for (List<DataPoint> row : data.getData()) {
            for (List<DataPoint> resultRow : group(row)) {
                result.addRow(resultRow);
            }
        }

        return result;
    }

    private List<List<DataPoint>> group(List<DataPoint> row) {
        List<List<DataPoint>> result = new ArrayList<>();
        DataPoint prevPoint = null;
        List<DataPoint> currentSet = new ArrayList<>();

        for (DataPoint point : row) {
            if (point.equals(prevPoint)) {
                currentSet.add(point);
            } else {
                prevPoint = point;
                if (!currentSet.isEmpty()) {
                    result.add(new ArrayList<>(currentSet));
                    currentSet.removeAll(currentSet);
                }
                currentSet.add(point);
            }
        }

        if (!currentSet.isEmpty())
            result.add(new ArrayList<>(currentSet));

        return result;
    }
}
