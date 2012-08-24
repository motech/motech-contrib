package org.motechproject.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-Diagnostics.xml")
public class DiagnosticResponseBuilderIT {

    @Autowired
    DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Test
    public void shouldVerifyTheMessageBuilt() {
        DiagnosticsResult diagnosticsResponse = new DiagnosticsResult("Aragorn", new DiagnosticsResult("Hrithik", true));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(Arrays.asList(diagnosticsResponse));

        assertTrue(responseMessage.contains("Aragorn Hrithik true"));
    }
}