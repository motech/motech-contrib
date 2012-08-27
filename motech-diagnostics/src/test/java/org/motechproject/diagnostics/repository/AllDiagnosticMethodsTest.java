package org.motechproject.diagnostics.repository;


import org.ektorp.CouchDbConnector;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.motechproject.diagnostics.util.TestClass;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationContext-DiagnosticsTest.xml")
public class AllDiagnosticMethodsTest extends SpringIntegrationTest {
    @Before
    @After
    public void setUpAndTearDown() {
        TestClass.methodExecutionCount = 0;
    }

    @Autowired
    private AllDiagnosticMethods allDiagnosticMethods;

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

    @Test
    public void shouldInvokeAllDiagnosticMethods() throws InvocationTargetException, IllegalAccessException {
        List<DiagnosticsResponse> diagnosticsResponses = allDiagnosticMethods.runAllDiagnosticMethods();

        List<DiagnosticsResponse> passedResponses = filter(having(on(DiagnosticsResponse.class).getResult().getStatus(), Matchers.equalTo(DiagnosticsStatus.PASS)), diagnosticsResponses);
        List<DiagnosticsResponse> failedResponses = filter(having(on(DiagnosticsResponse.class).getResult().getStatus(), Matchers.equalTo(DiagnosticsStatus.FAIL)), diagnosticsResponses);
        List<DiagnosticsResponse> unknownResponses = filter(having(on(DiagnosticsResponse.class).getResult().getStatus(), Matchers.equalTo(DiagnosticsStatus.UNKNOWN)), diagnosticsResponses);
        List<DiagnosticsResponse> warnResponses = filter(having(on(DiagnosticsResponse.class).getResult().getStatus(), Matchers.equalTo(DiagnosticsStatus.WARN)), diagnosticsResponses);

        assertEquals(7, TestClass.methodExecutionCount);
        assertEquals(5, passedResponses.size());
        assertEquals(3, failedResponses.size());
        assertEquals(2, unknownResponses.size());
        assertEquals(1, warnResponses.size());
    }
}