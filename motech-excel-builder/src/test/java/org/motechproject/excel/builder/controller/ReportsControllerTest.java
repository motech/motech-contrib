package org.motechproject.excel.builder.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.excel.builder.builder.ReportBuilder;

import java.io.OutputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ReportsControllerTest {

    private ReportsController reportsController;

    @Mock
    private ReportBuilder reportBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        reportsController = new ReportsController(reportBuilder);
    }

    @Test
    public void shouldCreateReportOfGivenType() throws Exception {
        FilterParams filterParams = new FilterParams();
        filterParams.put("key1", "value1");
        filterParams.put("key2", "value2");
        filterParams.put("emptyValue", "");

        String filterParamJson = new ObjectMapper().writer().writeValueAsString(filterParams);

        standaloneSetup(reportsController).build()
                .perform(get("/download/patientSummary.xls").param("filterParams", filterParamJson))
                .andExpect(status().isOk())
                .andExpect(header().string(ReportsController.CONTENT_DISPOSITION, "inline; filename=patientSummary.xls"))
                .andExpect(content().type(ReportsController.APPLICATION_VND_MS_EXCEL));

        verify(reportBuilder).buildReport(eq("patientSummary"), eq(filterParams.removeEmptyParams()), any(OutputStream.class));
    }
}
