package org.motechproject.timeseries.domain.service;

import org.motechproject.timeseries.domain.collections.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entities.TimeSeriesRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesSeries {

    private AllTimeSeriesRecords allTimeSeriesRecords;

    @Autowired
    public TimeSeriesSeries(AllTimeSeriesRecords allTimeSeriesRecords) {
        this.allTimeSeriesRecords = allTimeSeriesRecords;
    }

    public void capture(TimeSeriesRecord record) {
        allTimeSeriesRecords.add(record);
    }

    public TimeSeriesRecord recordForEntity(String externalId) {
        return allTimeSeriesRecords.withExternalId(externalId);
    }
}
