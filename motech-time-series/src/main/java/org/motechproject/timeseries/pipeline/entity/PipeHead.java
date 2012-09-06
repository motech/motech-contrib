package org.motechproject.timeseries.pipeline.entity;

import org.motechproject.timeseries.pipeline.valueobject.TimeSeriesSet;

import java.util.Map;

public interface PipeHead {

    public TimeSeriesSet process(Map<String, TimeSeriesSet> dataFromMultipleSources);
}
