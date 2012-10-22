package org.motechproject.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnostics.velocity.builder.DiagnosticResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class DiagnosticResponseBuilderIT {

    @Autowired
    DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Test
    public void shouldVerifyTheMessageBuilt() {
        DiagnosticsResult diagnosticsResponse = new DiagnosticsResult("Aragorn", true);
        ServiceResult serviceResult = new ServiceResult("serviceName", asList(diagnosticsResponse));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(asList(serviceResult));

        assertTrue(responseMessage.contains("Aragorn true"));
    }
}