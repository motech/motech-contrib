package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Clusterer implements PipeTransformation {

    private Double valueToCluster;

    public Clusterer(Double valueToCluster) {
        this.valueToCluster = valueToCluster;
    }

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data) {
        List<List<DataPoint>> result = new ArrayList<>();

        for (List<DataPoint> row : data) {
            for (List<DataPoint> resultRow : cluster(row)) {
                result.add(resultRow);
            }
        }

        return result;
    }

    private List<List<DataPoint>> cluster(List<DataPoint> row) {
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
