package org.motechproject.diagnostics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-Diagnostics.xml")
public class DiagnosticResponseBuilderIT {

    @Autowired
    DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Test
    public void shouldVerifyTheMessageBuilt() {
        DiagnosticsResponse diagnosticsResponse = new DiagnosticsResponse("Aragorn", new DiagnosticsResult(true, "Hrithik"));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(Arrays.asList(diagnosticsResponse));

        assertEquals("                    **************SUMMARY**************\n" +
                "\n" +
                "                            Aragorn - TRUE\n" +
                "            \n" +
                "                    ***********************************\n" +
                "\n" +
                "\n" +
                "!----------------------------------! Aragorn !----------------------------------!\n" +
                "\n" +
                "Status : TRUE\n" +
                "Message : Hrithik\n" +
                "\n",responseMessage);
    }
}