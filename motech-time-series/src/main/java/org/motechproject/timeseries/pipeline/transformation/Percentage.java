package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class Percentage implements PipeTransformation {

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> results = new ArrayList<>();
        for (List<DataPoint> row : data) {
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
            results.add(asList(result));
        }
        return results;
    }

    @Override
    public String name() {
        return "summation";
    }
}
