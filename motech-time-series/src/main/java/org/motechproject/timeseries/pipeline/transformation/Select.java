package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Select implements PipeTransformation {

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> result = new ArrayList<>();
        for (List<DataPoint> row : data) {
            List<DataPoint> resultRow = new ArrayList<>();
            for (DataPoint point : row) {
                DataPoint dataPoint = new DataPoint(point);
                if (point.getValue().equals(Double.valueOf(configuration.get("value")))) {
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
        return "select";
    }

}
