package org.motechproject.diagnostics.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;

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

        assertTrue(diagnosticsResult.getStatus());
        assertTrue(diagnosticsResult.getMessage().contains("database1"));
        assertTrue(diagnosticsResult.getMessage().contains("database2"));
    }
}
