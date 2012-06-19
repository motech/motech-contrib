package org.motechproject.export.model;

import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationBulkExportContext.xml")
public class AllExcelReportDataSourcesTest extends SpringIntegrationTest {

    @Autowired
    private AllExcelReportDataSources allExcelReportDataSources;

    @Test
    public void shouldReturnCorrectReportDataSource() {
        assertEquals("sampleExcelReports", allExcelReportDataSources.get("sampleExcelReports").name());
    }

    @Test
    public void shouldReturnNullForIncorrectReportPath() {
        assertNull(allExcelReportDataSources.get("incorrectReportPath"));
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

}
