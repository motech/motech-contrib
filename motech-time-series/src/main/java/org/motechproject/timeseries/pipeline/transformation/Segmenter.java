package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Segmenter implements PipeTransformation {

    private int slotSize;

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
