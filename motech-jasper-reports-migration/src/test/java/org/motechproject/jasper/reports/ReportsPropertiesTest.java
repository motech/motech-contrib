package org.motechproject.jasper.reports;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportsPropertiesTest {
    ReportsProperties reportsProperties = new ReportsProperties();

    @Test
    public void shouldGetAppropriateProperties() {
        assertEquals("http://localhost:8080/jasperserver/rest/role/", reportsProperties.getJasperRoleCreationURL());
        assertEquals("http://localhost:8080/jasperserver/rest/permission/", reportsProperties.getJasperPermissionsURL());
        assertEquals("http://localhost:8080/jasperserver/rest/resource/", reportsProperties.getJasperServerResourceURL());
        assertEquals("jasperadmin", reportsProperties.getJasperServerPassword());
        assertEquals("jasperadmin", reportsProperties.getJasperServerUserName());
        assertEquals("src/main/reports", reportsProperties.getReportsSourceLocation());
        assertEquals("Ananya", reportsProperties.getReportsRootLocation());
    }
}
