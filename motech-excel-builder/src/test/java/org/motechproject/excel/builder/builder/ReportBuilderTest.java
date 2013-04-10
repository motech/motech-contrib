package org.motechproject.excel.builder.builder;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.excel.builder.service.ReportService;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.excel.builder.builder.ReportBuilder.DATA;
import static org.motechproject.excel.builder.builder.ReportBuilder.GENERATED_ON;

public class ReportBuilderTest {

    ReportBuilder reportBuilder;

    @Mock
    private ExcelReportBuilder excelReportBuilder;
    @Mock
    private ReportService reportService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reportBuilder = new ReportBuilder(reportService, excelReportBuilder);
    }

    @Test
    public void shouldBuildExcelReportFromGivenReportType() {
        OutputStream outputStream = mock(OutputStream.class);
        String reportType = "reportType";
        List<Map<String, Object>> data = mock(List.class);
        FilterParams filterParams = mock(FilterParams.class);

        when(reportService.getData(reportType, filterParams)).thenReturn(data);

        reportBuilder.buildReport(reportType, filterParams, outputStream);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(excelReportBuilder).build(eq(outputStream), captor.capture(), eq("/xls/templates/" + reportType + ".xls"));
        Map params = captor.getValue();
        assertThat((List<Map<String, Object>>) params.get(DATA), is(data));
    }

    @Test
    public void shouldAddFilterKeysToReportParams(){
        FilterParams filterParams = new FilterParams();
        String filter_date_key = "filter_date_key";
        String filter_date_value = "22/03/2012";
        filterParams.put(filter_date_key, filter_date_value);

        String filter_string_key = "filter_key";
        String filter_string_value = "value";
        filterParams.put(filter_string_key, filter_string_value);

        String generatedOn = new DateTime().toString("dd/MM/yyyy hh:mm:ss");
        filterParams.put(GENERATED_ON, generatedOn);

        HashMap patient1 = new HashMap(){{
            put("uniq_id", 123);
        }};

        List<Map<String, Object>> resultSet = new ArrayList<>();
        resultSet.add(patient1);

        Map<String, Object> reportParams = reportBuilder.setReportParameters(resultSet, filterParams);
        assertThat(reportParams.size(), is(4));
        assertThat((List<Map<String, Object>>) reportParams.get(DATA), is(resultSet));
        assertThat((String) reportParams.get(GENERATED_ON), is(generatedOn));
        assertThat((String)reportParams.get(filter_date_key), is(filter_date_value));
        assertThat((String)reportParams.get(filter_string_key), is(filter_string_value));


        Map<String, Object> reportParamsWithEmptyFilters = reportBuilder.setReportParameters(resultSet, new FilterParams());
        assertThat(reportParamsWithEmptyFilters.size(), is(2));
        assertThat((List<Map<String, Object>>) reportParamsWithEmptyFilters.get(DATA), is(resultSet));
        assertThat((String) reportParamsWithEmptyFilters.get(GENERATED_ON), is(generatedOn));

    }
}
