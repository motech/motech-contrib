package org.motechproject.timeseries.pipeline.contract;

public interface PipeReader {

    public TimeSeriesSet process(TimeSeriesSet data);
}
