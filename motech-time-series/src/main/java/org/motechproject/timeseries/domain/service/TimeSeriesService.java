package org.motechproject.timeseries.domain.service;

import org.motechproject.timeseries.domain.collection.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesService {

    private AllTimeSeriesRecords allTimeSeriesRecords;

    @Autowired
    public TimeSeriesService(AllTimeSeriesRecords allTimeSeriesRecords) {
        this.allTimeSeriesRecords = allTimeSeriesRecords;
    }

    public void capture(TimeSeriesRecord record) {
        allTimeSeriesRecords.add(record);
    }

    public TimeSeriesRecord recordForEntity(String externalId) {
        return allTimeSeriesRecords.withExternalId(externalId);
    }
}
