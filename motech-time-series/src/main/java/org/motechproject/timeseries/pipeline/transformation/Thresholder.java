package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

public class Thresholder implements PipeTransformation {

    private Double threshold;
    private boolean lessThan;

    public Thresholder(Double threshold) {
        this.threshold = threshold;
        this.lessThan = true;
    }

    public Thresholder(Double threshold, boolean lessThan) {
        this.threshold = threshold;
        this.lessThan = lessThan;
    }

    @Override
    public Object process(List<Double> data) {
        List<Double> result = new ArrayList<>();
        for (Double point : data) {
            if (conditionTriggered(point)) {
                result.add(1d);
                if (lessThan)
                    System.out.println("Value " + point + " is less than threshold " + threshold);
                else
                    System.out.println("Value " + point + " is greater than threshold " + threshold);
            } else {
                result.add(0d);
            }
        }
        return result;
    }

    private boolean conditionTriggered(Double point) {
        return (lessThan) ? point < threshold : point > threshold;
    }
}
