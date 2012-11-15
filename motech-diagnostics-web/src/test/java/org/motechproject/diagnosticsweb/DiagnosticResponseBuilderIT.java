package org.motechproject.diagnosticsweb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnosticsweb.velocity.builder.DiagnosticResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static org.motechproject.diagnostics.response.Status.Success;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class DiagnosticResponseBuilderIT {

    @Autowired
    DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Test
    public void shouldVerifyTheMessageBuilt() {
        DiagnosticsResult diagnosticsResponse = new DiagnosticsResult("Apache", "is running", Success);
        ServiceResult serviceResult = new ServiceResult("serviceName", asList(diagnosticsResponse));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(asList(serviceResult));

        Assert.assertTrue(responseMessage.contains("Apache is running"));
    }

    @Test
    public void shouldVerifyTheMessageBuiltForNestedDiagnosticResults() {
        DiagnosticsResult diagnosticsResponse = new DiagnosticsResult("Apache", "is running", Success);
        DiagnosticsResult nestedDiagnosticResponse = new DiagnosticsResult("Apache Port", "80", Success);
        diagnosticsResponse.add(nestedDiagnosticResponse);
        ServiceResult serviceResult = new ServiceResult("serviceName", asList(diagnosticsResponse));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(asList(serviceResult));

        Assert.assertTrue(responseMessage.contains("Apache Port 80"));
    }


}