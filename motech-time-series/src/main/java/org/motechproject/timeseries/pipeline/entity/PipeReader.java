package org.motechproject.timeseries.pipeline.entity;

import org.motechproject.timeseries.pipeline.valueobject.TimeSeriesSet;

public interface PipeReader {

    public TimeSeriesSet process(TimeSeriesSet data);
}
