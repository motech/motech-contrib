package org.motechproject.timeseries.domain.service;

import org.motechproject.timeseries.domain.collection.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.rules.service.TimeSeriesRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesService {

    private AllTimeSeriesRecords allTimeSeriesRecords;
    private TimeSeriesRulesService rulesService;

    @Autowired
    public TimeSeriesService(AllTimeSeriesRecords allTimeSeriesRecords, TimeSeriesRulesService rulesService) {
        this.allTimeSeriesRecords = allTimeSeriesRecords;
        this.rulesService = rulesService;
    }

    public void capture(TimeSeriesRecord record) {
        allTimeSeriesRecords.add(record);
        rulesService.executeRules(record, "onRecordEvent");
    }

    public TimeSeriesRecord recordForEntity(String externalId) {
        return allTimeSeriesRecords.withExternalId(externalId);
    }
}
