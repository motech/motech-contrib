package org.motechproject.export.model;

import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration(locations = "classpath*:/applicationBulkExportContext.xml")
public class AllExcelExportProcessorsTest extends SpringIntegrationTest {

    @Autowired
    private AllExcelExportProcessors allExcelExportProcessors;

    @Test
    public void shouldReturnCorrectReportDataSource() {
        assertEquals("sampleExcel", allExcelExportProcessors.get("sampleExcel").name());
    }

    @Test
    public void shouldReturnNullForIncorrectReportPath() {
        assertNull(allExcelExportProcessors.get("incorrectReportPath"));
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

}
