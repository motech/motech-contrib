package org.motechproject.export.service;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.export.service.sample.SampleCSVDataSource;
import org.motechproject.export.service.sample.SampleCSVDataSourceWithParameter;
import org.motechproject.export.service.sample.SampleExcelDataSource;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;


@ContextConfiguration(locations = "classpath*:/applicationBulkExportContext.xml")
public class ExportServiceIT extends SpringIntegrationTest {

    @Autowired
    private ExportService exportService;
    @Autowired
    private SampleExcelDataSource sampleExcelDataSource;
    @Autowired
    private SampleCSVDataSourceWithParameter sampleCSVDataSourceWithParameter;
    @Autowired
    private SampleCSVDataSource sampleCSVDataSource;
    private MockHttpServletResponse mockHttpServletResponse;

    @Before
    public void setup() throws IOException {
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void shouldIdentifyAppropriateExcelController() throws IOException {
        assertFalse(sampleExcelDataSource.isCalled);
        exportService.exportAsExcel("sampleExcel", "sampleExcel", mockHttpServletResponse.getOutputStream());
        assertTrue(sampleExcelDataSource.isCalled);
    }

    @Test
    public void shouldIdentifyAppropriateCSVController() throws IOException {
        assertFalse(sampleCSVDataSource.isCalled);
        exportService.exportAsCSV("sampleCSV", mockHttpServletResponse.getWriter());
        assertTrue(sampleCSVDataSource.isCalled);
        assertEquals(expectedContent(), mockHttpServletResponse.getContentAsString());
    }

    @Test
    public void shouldExportAsCSVWithTheAppropriateParametersPassedToTheDataProviderMethod() throws UnsupportedEncodingException {
        Object parameterMap = new Object();
        assertFalse(sampleCSVDataSourceWithParameter.isCalled);
        exportService.exportAsCSV("sampleCSVWithParameter", mockHttpServletResponse.getWriter(), parameterMap);
        assertTrue(sampleCSVDataSourceWithParameter.isCalled);
        assertEquals(parameterMap, sampleCSVDataSourceWithParameter.parameters);
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
