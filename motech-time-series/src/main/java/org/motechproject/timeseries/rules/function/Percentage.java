package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

public class Percentage {

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet results = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            DataPoint result = new DataPoint();
            Double numerator = 0d;
            Double denominator = 0d;
            for (DataPoint point : row) {
                result.startFrom(point.getStartTime());
                result.extendTill(point.getEndTime());
                numerator += point.getValue();
                denominator += point.getIdeal();
                result.setValue((numerator / denominator) * 100);
            }
            results.addRow(asList(result));
        }
        return results;
    }
}
