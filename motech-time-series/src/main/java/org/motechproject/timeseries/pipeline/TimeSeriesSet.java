package org.motechproject.timeseries.pipeline;


import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TimeSeriesSet {

    private List<List<DataPoint>> data;

    public TimeSeriesSet() {
        data = new ArrayList<>();
    }

    public TimeSeriesSet(TimeSeriesRecord record) {
        this.data = asList(record.allDataPoints());
    }

    public TimeSeriesSet addRow(List<DataPoint> row) {
        data.add(row);
        return this;
    }

    public TimeSeriesSet apply(PipeTransformation reader, Map<String, String> configuration) {
        if (!data.isEmpty()) {
            this.data = tansform(reader.process(data, configuration));
        }
        return this;
    }

    private List<List<DataPoint>> tansform(List<List<DataPoint>> data) {
        boolean allListsHaveSingleElement = true;
        for (List<DataPoint> row : data) {
            allListsHaveSingleElement &= row.size() == 1;
            if (!allListsHaveSingleElement)
                break;
        }
        if (allListsHaveSingleElement) {
            List<DataPoint> result = new ArrayList<>();
            for (List<DataPoint> row : data) {
                result.add(row.get(0));
            }
            return asList(result);
        } else {
            return data;
        }
    }
}

