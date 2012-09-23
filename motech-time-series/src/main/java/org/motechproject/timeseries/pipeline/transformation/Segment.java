package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

@Component
public class Segment implements PipeTransformation {


    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
        List<List<DataPoint>> result = new ArrayList<>();

        for (List<DataPoint> row : data) {
            for (List<DataPoint> resultRow : group(row, parseInt(configuration.get("slotSize")))) {
                result.add(resultRow);
            }
        }

        return result;
    }

    @Override
    public String name() {
        return "segment";
    }

    private List<List<DataPoint>> group(List<DataPoint> row, int slotSize) {
        List<List<DataPoint>> result = new ArrayList<>();
        List<DataPoint> currentSet = new ArrayList<>();
        int i = 0;

        for (DataPoint point : row) {
            if (i < slotSize) {
                currentSet.add(point);
                i++;
            } else {
                result.addAll(asList(currentSet));
                currentSet.removeAll(currentSet);
                i = 0;
            }
        }
        if (!currentSet.isEmpty()) {
            result.addAll(asList(currentSet));
        }
        return result;
    }
}
