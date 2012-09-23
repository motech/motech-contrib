package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Group implements PipeTransformation {


    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> result = new ArrayList<>();

        for (List<DataPoint> row : data) {
            for (List<DataPoint> resultRow : group(row)) {
                result.add(resultRow);
            }
        }

        return result;
    }

    @Override
    public String name() {
        return "group";
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
