package org.motechproject.timeseries.pipeline;

import java.util.List;

public interface PipeTransformation {

    public Object process(List<Double> data);
}
