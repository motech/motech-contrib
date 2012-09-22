package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

public class Threshold implements PipeTransformation {

    private Double threshold;
    private boolean lessThan;

    public Threshold(Double threshold) {
        this.threshold = threshold;
        this.lessThan = true;
    }

    public Threshold(Double threshold, boolean lessThan) {
        this.threshold = threshold;
        this.lessThan = lessThan;
    }

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data) {
        List<List<DataPoint>> result = new ArrayList<>();
        for (List<DataPoint> row : data) {
            List<DataPoint> resultRow = new ArrayList<>();
            for (DataPoint point : row) {
                DataPoint dataPoint = new DataPoint(point);
                if (conditionTriggered(point)) {
                    System.out.println("Value " + point.getValue() + " has triggered the threshold " + threshold);
                    dataPoint.setValue(1d);
                } else {
                    dataPoint.setValue(0d);
                }
                resultRow.add(dataPoint);
            }
            result.add(new ArrayList<>(resultRow));
        }
        return result;
    }

    private boolean conditionTriggered(DataPoint point) {
        return (lessThan) ? point.getValue() < threshold : point.getValue() > threshold;
    }
}
