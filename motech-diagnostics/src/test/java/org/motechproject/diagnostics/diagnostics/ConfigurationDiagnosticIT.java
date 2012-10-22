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
        DiagnosticsResult diagnosticsResults = configurationDiagnostic.performDiagnosis();

        assertEquals("true", diagnosticsResults.getResults().get(0).getValue());
        assertTrue(containsResultWithName("activemq", diagnosticsResults.getResults()));
        assertTrue(containsNestedResultWithName("queue.for.events", "QueueForEvents", diagnosticsResults.getResults()));
        assertTrue(containsResultWithName("postgres", diagnosticsResults.getResults()));
        assertTrue(containsNestedResultWithName("jdbc.username", "postgres", diagnosticsResults.getResults()));
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
            if (!result.getResults().isEmpty()) {
                List<DiagnosticsResult> nestedResults = result.getResults();
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