package org.motechproject.timeseries.pipeline.contract;

import java.util.Map;

public interface PipeHead {

    public TimeSeriesSet process(Map<String, TimeSeriesSet> dataFromMultipleSources);
}
