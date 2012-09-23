package org.motechproject.timeseries.pipeline;

import org.motechproject.timeseries.domain.valueobject.DataPoint;

import java.util.List;
import java.util.Map;

public interface PipeTransformation {

    public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration);
    public String name();
}
