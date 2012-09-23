package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Threshold implements PipeTransformation {

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> result = new ArrayList<>();
        for (List<DataPoint> row : data) {
            List<DataPoint> resultRow = new ArrayList<>();
            for (DataPoint point : row) {
                DataPoint dataPoint = new DataPoint(point);
                if (conditionTriggered(point, configuration)) {
                    System.out.println("Value " + point.getValue() + " has triggered the threshold " + configuration.get("threshold"));
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

    @Override
    public String name() {
        return "threshold";
    }

    private boolean conditionTriggered(DataPoint point, Map<String, String> configuration) {
        return (Boolean.valueOf(configuration.get("lessThan"))) ?
                point.getValue() < Double.valueOf(configuration.get("threshold"))
                : point.getValue() > Double.valueOf(configuration.get("threshold"));
    }
}
