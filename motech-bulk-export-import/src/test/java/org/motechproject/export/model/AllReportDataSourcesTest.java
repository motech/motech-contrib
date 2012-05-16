package org.motechproject.export.model;

import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationBulkExportImportContext.xml")
public class AllReportDataSourcesTest extends SpringIntegrationTest {

    @Autowired
    private AllReportDataSources allReportDataSources;

    @Test
    public void shouldReturnCorrectReportDataSource() {
        assertEquals("sampleReports", allReportDataSources.get("sampleReports").name());
    }

    @Test
    public void shouldReturnNullForIncorrectReportPath() {
        assertNull(allReportDataSources.get("incorrectReportPath"));
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

}
