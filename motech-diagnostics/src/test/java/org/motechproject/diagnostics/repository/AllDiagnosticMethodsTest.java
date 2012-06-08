package org.motechproject.diagnostics.repository;


import org.ektorp.CouchDbConnector;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.util.TestClass;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationContext-Diagnostics.xml")
public class AllDiagnosticMethodsTest extends SpringIntegrationTest {

    @Autowired
    private AllDiagnosticMethods allDiagnosticMethods;

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

    @Test
    public void shouldInvokeAllDiagnosticMethods() throws InvocationTargetException, IllegalAccessException {
        List<DiagnosticsResponse> diagnosticsResponses = allDiagnosticMethods.runAllDiagnosticMethods();

        assertEquals(2, TestClass.methodExecutionCount);
        assertTrue(diagnosticsResponses.get(0).getResult().getStatus());
        assertFalse(diagnosticsResponses.get(1).getResult().getStatus());
    }
}
