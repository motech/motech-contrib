package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class Cluster implements PipeTransformation {


    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> result = new ArrayList<>();

        for (List<DataPoint> row : data) {
            for (List<DataPoint> resultRow : cluster(row, Double.valueOf(configuration.get("valueToCluster")))) {
                result.add(resultRow);
            }
        }

        return result;
    }

    @Override
    public String name() {
        return "cluster";
    }

    private List<List<DataPoint>> cluster(List<DataPoint> row, Double valueToCluster) {
        List<List<DataPoint>> result = new ArrayList<>();
        List<DataPoint> currentSet = new ArrayList<>();

        for (DataPoint point : row) {
            if (point.getValue().equals(valueToCluster)) {
                currentSet.add(point);
            } else {
                if (!currentSet.isEmpty()) {
                    result.add(new ArrayList<>(currentSet));
                    currentSet.removeAll(currentSet);
                }
                result.add(asList(point));
            }
        }

        if (!currentSet.isEmpty())
            result.add(new ArrayList<>(currentSet));

        return result;
    }
}
