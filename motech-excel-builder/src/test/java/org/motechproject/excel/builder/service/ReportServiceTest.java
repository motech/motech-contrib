package org.motechproject.excel.builder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.response.QueryResult;
import org.motechproject.bigquery.service.BigQueryService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportServiceTest {

    @Mock
    BigQueryService bigQueryService;
    ReportService reportService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reportService = new ReportService(bigQueryService);
    }

    @Test
    public void shouldReturnResultSetForGivenReportType(){
        String reportType = "reportType";

        List<Map<String, Object>> resultSet = mock(List.class);
        QueryResult queryResult = new QueryResult(resultSet);

        when(bigQueryService.executeQuery(reportType, new FilterParams())).thenReturn(queryResult);

        List<Map<String, Object>> expectedResultSet = reportService.getData(reportType, new FilterParams());

        assertEquals(expectedResultSet, resultSet);
    }
}
