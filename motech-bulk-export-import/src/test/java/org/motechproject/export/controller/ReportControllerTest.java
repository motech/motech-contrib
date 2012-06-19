package org.motechproject.export.controller;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.export.controller.sample.SampleCSVReportController;
import org.motechproject.export.controller.sample.SampleExcelReportController;
import org.motechproject.export.writer.ExcelWriter;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.export.writer.CSVWriter.CONTENT_DISPOSITION;
import static org.motechproject.export.writer.CSVWriter.TEXT_CSV;


@ContextConfiguration(locations = "classpath*:/applicationBulkExportContext.xml")
public class ReportControllerTest extends SpringIntegrationTest {

    private MockHttpServletResponse mockHttpServletResponse;
    @Autowired
    private ReportController reportController;
    @Autowired
    private SampleExcelReportController sampleExcelReportController;
    @Autowired
    private SampleCSVReportController sampleCSVReportController;

    @Before
    public void setup() throws IOException {
        initMocks(this);
        mockHttpServletResponse = new MockHttpServletResponse();
        sampleExcelReportController.isCalled = false;
        sampleCSVReportController.isCalled = false;
    }

    @Test
    public void shouldIdentifyAppropriateExcelController() {
        assertFalse(sampleExcelReportController.isCalled);
        String reportName = "sampleReport";
        reportController.createExcelReport("sampleExcelReports", reportName, mockHttpServletResponse);
        assertTrue(sampleExcelReportController.isCalled);
        assertEquals(ExcelWriter.APPLICATION_VND_MS_EXCEL, mockHttpServletResponse.getContentType());
        assertEquals("inline; filename=" + reportName + ".xls", mockHttpServletResponse.getHeaderValue(ExcelWriter.CONTENT_DISPOSITION));
    }

    @Test
    public void shouldIdentifyAppropriateCSVController() throws UnsupportedEncodingException {
        assertFalse(sampleCSVReportController.isCalled);
        String reportName = "sampleCSVReport";
        reportController.createCSVReport("sampleCSVReports", reportName, mockHttpServletResponse);
        assertTrue(sampleCSVReportController.isCalled);
        assertEquals(TEXT_CSV, mockHttpServletResponse.getContentType());
        assertEquals("inline; filename=" + reportName + ".csv", mockHttpServletResponse.getHeaderValue(CONTENT_DISPOSITION));
        assertEquals(expectedContent(), mockHttpServletResponse.getContentAsString());
    }

    private String expectedContent() {
        return "Id,Custom column name,Boolean Value\n" +
                "id1,title,true\n" +
                "id2,title,true\n" +
                "id3,title,true\n";
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

}
