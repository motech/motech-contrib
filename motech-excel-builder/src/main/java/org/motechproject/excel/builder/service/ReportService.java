package org.motechproject.excel.builder.service;


import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.service.BigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private BigQueryService bigQueryService;

    @Autowired
    public ReportService(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    public List<Map<String, Object>> getData(String reportType, FilterParams filterParams) {
        return bigQueryService.executeQuery(reportType, filterParams).getContent();
    }
}
