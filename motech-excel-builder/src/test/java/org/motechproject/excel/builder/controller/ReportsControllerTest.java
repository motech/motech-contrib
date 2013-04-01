package org.motechproject.excel.builder.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.excel.builder.builder.ReportBuilder;

import java.io.OutputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
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

        standaloneSetup(reportsController).build()
                .perform(get("/download/patientSummary.xls"))
                .andExpect(status().isOk())
                .andExpect(header().string(ReportsController.CONTENT_DISPOSITION, "inline; filename=patientSummary.xls"))
                .andExpect(content().type(ReportsController.APPLICATION_VND_MS_EXCEL));

        verify(reportBuilder).buildReport(eq("patientSummary"), any(OutputStream.class));
    }
}
