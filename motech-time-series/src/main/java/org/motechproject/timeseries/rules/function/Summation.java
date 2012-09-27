package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

public class Summation {

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet results = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            DataPoint result = new DataPoint();
            for (DataPoint point : row) {
                result.startFrom(point.getStartTime());
                result.extendTill(point.getEndTime());
                result.setValue(result.getValue() + point.getValue());

            }
            results.addRow(asList(result));
        }
        return results;
    }
}
