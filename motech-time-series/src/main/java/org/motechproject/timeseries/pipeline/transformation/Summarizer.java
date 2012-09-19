package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Summarizer implements PipeTransformation {

    @Override
    public List<List<DataPoint>> process(List<List<DataPoint>> data) {
        List<List<DataPoint>> results = new ArrayList<>();
        for (List<DataPoint> row : data) {
            DataPoint result = new DataPoint();
            for (DataPoint point : row) {
                result.startFrom(point.getStartTime());
                result.extendTill(point.getEndTime());
                result.setValue(result.getValue() + point.getValue());

            }
            results.add(asList(result));
        }
        return results;
    }
}