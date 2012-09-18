package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

public class Counter implements PipeTransformation {

    private Double value;

    public Counter(Double value) {
        this.value = value;
    }

    @Override
    public Object process(List<Double> data) {
        List<Double> result = new ArrayList<>();
        for (Double point : data) {
            if (point.equals(value)) {
                result.add(1d);
            } else {
                result.add(0d);
            }
        }
        return result;
    }

}
