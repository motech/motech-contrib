package org.motechproject.excel.builder.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.excel.builder.service.QueryService;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.excel.builder.builder.ReportBuilder.DATA;

public class ReportBuilderTest {

    ReportBuilder reportBuilder;

    @Mock
    private ExcelReportBuilder excelReportBuilder;
    @Mock
    private QueryService queryService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reportBuilder = new ReportBuilder(queryService, excelReportBuilder);
    }

    @Test
    public void shouldBuildExcelReportFromGivenReportType() {
        OutputStream outputStream = mock(OutputStream.class);
        String reportType = "reportType";
        List<Map<String, Object>> data = mock(List.class);
        when(queryService.getData(reportType)).thenReturn(data);

        reportBuilder.buildReport(reportType, outputStream);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(excelReportBuilder).build(eq(outputStream), captor.capture(), eq(reportType + ".xls"));
        Map params = captor.getValue();
        assertThat((List<Map<String, Object>>) params.get(DATA), is(data));
    }
}
