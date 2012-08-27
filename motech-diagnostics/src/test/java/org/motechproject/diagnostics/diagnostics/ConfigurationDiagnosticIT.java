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
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class ConfigurationDiagnosticIT {

    @Autowired
    private ConfigurationDiagnostic configurationDiagnostic;

    @Test
    public void shouldPrintAllPropertiesToLog() throws JMSException {
        DiagnosticsResult diagnosticsResult = configurationDiagnostic.performDiagnosis();

        assertEquals(DiagnosticsStatus.PASS, diagnosticsResult.getStatus());
        assertTrue(diagnosticsResult.getMessage().contains("activemq"));
        assertTrue(diagnosticsResult.getMessage().contains("queue.for.events=QueueForEvents"));
        assertTrue(diagnosticsResult.getMessage().contains("postgres"));
        assertTrue(diagnosticsResult.getMessage().contains("jdbc.username=postgres"));
    }
}