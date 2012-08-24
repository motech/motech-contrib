package org.motechproject.diagnostics.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import java.util.List;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class ConfigurationDiagnosticIT {

    @Autowired
    private ConfigurationDiagnostic configurationDiagnostic;

    @Test
    public void shouldPrintAllProperties() throws JMSException {
        DiagnosticsResult<List<DiagnosticsResult>> diagnosticsResults = configurationDiagnostic.performDiagnosis();

        assertEquals("true", diagnosticsResults.getValue().get(0).getValue());
        assertTrue(containsResultWithName("activemq", diagnosticsResults.getValue()));
        assertTrue(containsNestedResultWithName("queue.for.events", "QueueForEvents", diagnosticsResults.getValue()));
        assertTrue(containsResultWithName("postgres", diagnosticsResults.getValue()));
        assertTrue(containsNestedResultWithName("jdbc.username", "postgres", diagnosticsResults.getValue()));
    }

    @Test
    public void shouldNotPerformDiagnosisIfPropertyFileMapIsNull() throws JMSException {
        ConfigurationDiagnostic configurationDiagnostic = new ConfigurationDiagnostic();
        DiagnosticsResult diagnosticsResult = configurationDiagnostic.performDiagnosis();
        assertNull(diagnosticsResult);
    }

    private boolean containsResultWithName(String name, List<DiagnosticsResult> results) {
        for (DiagnosticsResult result : results) {
            if (result.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNestedResultWithName(String nestedElementName,
                                                 String nestedElementValue,
                                                 List<DiagnosticsResult> results) {
        for (DiagnosticsResult result : results) {
            if (result.getValue() instanceof List) {
                List<DiagnosticsResult> nestedResults = (List<DiagnosticsResult>) result.getValue();
                for (DiagnosticsResult nestedResult : nestedResults) {
                    if (nestedResult.getName().equalsIgnoreCase(nestedElementName)) {
                        return nestedResult.getValue().equals(nestedElementValue);
                    }
                }
            }
        }
        return false;
    }
}