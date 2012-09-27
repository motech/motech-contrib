package org.motechproject.timeseries.domain.valueobject;


import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;

import java.util.ArrayList;
import java.util.List;

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

    public List<List<DataPoint>> getData() {
        SingularResult singularData = singularData();
        if (singularData.isValid()) {
            return asList(singularData.getResult());
        } else {
            return data;
        }
    }

    private SingularResult singularData() {
        boolean allListsHaveSingleElement = true;
        List<DataPoint> result = new ArrayList<>();
        for (List<DataPoint> row : data) {
            allListsHaveSingleElement &= row.size() == 1;
            if (!allListsHaveSingleElement) {
                return new SingularResult(false);
            } else {
                result.add(row.get(0));
            }
        }
        return new SingularResult(true, result);
    }
}

