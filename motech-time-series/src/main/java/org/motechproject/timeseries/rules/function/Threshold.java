package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class Threshold {

    private Double value;
    private boolean lessThan;

    public Threshold(Double value, boolean lessThan) {
        this.value = value;
        this.lessThan = lessThan;
    }

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet result = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            List<DataPoint> resultRow = new ArrayList<>();
            for (DataPoint point : row) {
                DataPoint dataPoint = new DataPoint(point);
                if (conditionTriggered(point)) {
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

    private boolean conditionTriggered(DataPoint point) {
        return (lessThan) ?
                point.getValue() < value
                : point.getValue() > value;
    }
}
