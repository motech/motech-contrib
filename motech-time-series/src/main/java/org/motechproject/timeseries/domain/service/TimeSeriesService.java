package org.motechproject.timeseries.domain.service;

import org.motechproject.timeseries.domain.collection.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.pipeline.service.TimeSeriesOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesService {

    private AllTimeSeriesRecords allTimeSeriesRecords;
    private TimeSeriesOperationService operationService;

    @Autowired
    public TimeSeriesService(AllTimeSeriesRecords allTimeSeriesRecords, TimeSeriesOperationService operationService) {
        this.allTimeSeriesRecords = allTimeSeriesRecords;
        this.operationService = operationService;
    }

    public void capture(TimeSeriesRecord record) {
        allTimeSeriesRecords.add(record);
        operationService.triggerPipes(record.getExternalId(), "onRecordEvent");
    }

    public TimeSeriesRecord recordForEntity(String externalId) {
        return allTimeSeriesRecords.withExternalId(externalId);
    }
}
