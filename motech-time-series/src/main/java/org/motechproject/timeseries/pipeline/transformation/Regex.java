package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.matches;

@Component
public class Regex implements PipeTransformation {

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> results = new ArrayList<>();
        for (List<DataPoint> row : data) {
            DataPoint result = new DataPoint();
            result.startFrom(row.get(0).getStartTime());
            result.extendTill(row.get(row.size() - 1).getEndTime());
            result.setValue(matches(configuration.get("regexp"), asString(row)) ? 1d : 0d);
            results.add(asList(result));
        }
        return results;
    }

    @Override
    public String name() {
        return "regex";
    }

    private String asString(List<DataPoint> row) {
        StringBuilder result = new StringBuilder();
        for (DataPoint point : row) {
            result.append(point.getValue().toString());
        }
        return result.toString();
    }
}
