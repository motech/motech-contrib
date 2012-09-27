package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class Select {

    private Double value;

    public Select(Double value) {
        this.value = value;
    }

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet result = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            List<DataPoint> resultRow = new ArrayList<>();
            for (DataPoint point : row) {
                DataPoint dataPoint = new DataPoint(point);
                if (point.getValue().equals(value)) {
                    dataPoint.setValue(1d);
                } else {
                    dataPoint.setValue(0d);
                }
                resultRow.add(dataPoint);
            }
            result.addRow(new ArrayList<>(resultRow));
        }
        return result;
    }
}
