package org.motechproject.timeseries.pipeline;

import java.util.List;

public class PipeLine {

    private List<PipeTransformation> transformations;

    public PipeLine(List<PipeTransformation> transformations) {
        this.transformations = transformations;
    }

    public TimeSeriesSet process(TimeSeriesSet set) {
        TimeSeriesSet currentStateOfResult = set;
        for (PipeTransformation transformation : transformations) {
            currentStateOfResult = currentStateOfResult.apply(transformation);
        }
        return currentStateOfResult;
    }
}
