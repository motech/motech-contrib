package org.motechproject.diagnostics.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class CouchDBDiagnosticIT {

    @Autowired
    CouchDBDiagnostic couchDBDiagnostic;

    @Test
    public void shouldPerformCouchDBDiagnostic() {
        DiagnosticsResult diagnosticsResult = couchDBDiagnostic.performDiagnosis();
        System.out.println(diagnosticsResult.getMessage());
        assertNotNull(diagnosticsResult);
    }

    @Test
    public void shouldPrintAllDatabases() throws JMSException {
        DiagnosticsResult diagnosticsResult = couchDBDiagnostic.performDiagnosis();

        assertEquals(DiagnosticsStatus.FAIL, diagnosticsResult.getStatus());
        String diagnosticsMessage = diagnosticsResult.getMessage();
        assertTrue(diagnosticsMessage.contains("_users : HTTP Status Code: 200"));
        assertTrue(diagnosticsMessage.contains("unknown-database : HTTP Status Code: 404"));
    }
}
