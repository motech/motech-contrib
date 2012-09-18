package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.List;

public class Summarizer implements PipeTransformation {

    @Override
    public Object process(List<Double> data) {
        Double result = 0d;
        for (Double point : data) {
            result += point;
        }
        return result;
    }
}
