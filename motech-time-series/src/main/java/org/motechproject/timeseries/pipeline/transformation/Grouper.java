package org.motechproject.timeseries.pipeline.transformation;

import org.motechproject.timeseries.pipeline.PipeTransformation;

import java.util.ArrayList;
import java.util.List;

public class Grouper implements PipeTransformation {


    @Override
    public Object process(List<Double> data) {
        List<List<Double>> sequence = new ArrayList<>();
        Double prevPoint = null;
        List<Double> currentSet = new ArrayList<>();
        for (Double point : data) {
            if (point.equals(prevPoint)) {
                currentSet.add(point);
            } else {
                prevPoint = point;
                if (!currentSet.isEmpty()) {
                    sequence.add(new ArrayList<>(currentSet));
                    currentSet.removeAll(currentSet);
                }
                currentSet.add(point);
            }
        }
        if (!currentSet.isEmpty())
            sequence.add(new ArrayList<Double>(currentSet));
        return sequence;
    }
}
