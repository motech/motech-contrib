package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

public class Grouper implements PipeTransformation {


    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data) {
        List<List<DataPoint>> result = new ArrayList<>();

        for (List<DataPoint> row : data) {
            for (List<DataPoint> resultRow : group(row)) {
                result.add(resultRow);
            }
        }

        return result;
    }

    private List<List<DataPoint>> group(List<DataPoint> row) {
        List<List<DataPoint>> result = new ArrayList<>();
        DataPoint prevPoint = null;
        List<DataPoint> currentSet = new ArrayList<>();

        for (DataPoint point : row) {
            if (point.equals(prevPoint)) {
                currentSet.add(point);
            } else {
                prevPoint = point;
                if (!currentSet.isEmpty()) {
                    result.add(new ArrayList<>(currentSet));
                    currentSet.removeAll(currentSet);
                }
                currentSet.add(point);
            }
        }

        if (!currentSet.isEmpty())
            result.add(new ArrayList<>(currentSet));

        return result;
    }
}
