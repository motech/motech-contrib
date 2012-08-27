package org.motechproject.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class DiagnosticResponseBuilderIT {

    @Autowired
    DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Test
    public void shouldVerifyTheMessageBuilt() {
        DiagnosticsResponse diagnosticsResponse = new DiagnosticsResponse("Aragorn", new DiagnosticsResult(DiagnosticsStatus.PASS, "Hrithik"));
        String responseMessage = diagnosticResponseBuilder.createResponseMessage(Arrays.asList(diagnosticsResponse));

        assertEquals("                    **************SUMMARY**************\n" +
                "\n" +
                "                            Aragorn - PASS\n" +
                "            \n" +
                "                    ***********************************\n" +
                "\n" +
                "\n" +
                "!----------------------------------! Aragorn !----------------------------------!\n" +
                "\n" +
                "Hrithik\n" +
                "\n", responseMessage);
    }
}