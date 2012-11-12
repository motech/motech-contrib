package org.motechproject.diagnostics.diagnostics.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.diagnostics.ActiveMQDiagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;

import java.net.MalformedURLException;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class ActiveMQDiagnosticIT {

    @Autowired
    private ActiveMQDiagnostic activeMQDiagnostic;

    @Test
    public void shouldCheckActiveMQConnection() throws JMSException {
        DiagnosticsResult diagnosticsResult = activeMQDiagnostic.performDiagnosis();
        assertNotNull(diagnosticsResult);
        assertEquals("Active MQ Port Is Active", diagnosticsResult.getName());
        assertEquals("true", diagnosticsResult.getValue());
    }


    @Test
    public void shouldCheckActiveMQQueueSizes() throws JMSException, MalformedURLException {
        DiagnosticsResult diagnosticsResult = activeMQDiagnostic.queueSizes();
        assertNotNull(diagnosticsResult);
    }
}