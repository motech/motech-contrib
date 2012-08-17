package org.motechproject.diagnostics.repository;


import org.ektorp.CouchDbConnector;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
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

        List<DiagnosticsResponse> testDiagnostics1Response = filter(having(on(DiagnosticsResponse.class).getName(), Matchers.equalTo("testDiagnostics1")), diagnosticsResponses);
        List<DiagnosticsResponse> testDiagnostics2Response = filter(having(on(DiagnosticsResponse.class).getName(), Matchers.equalTo("testDiagnostics2")), diagnosticsResponses);
        List<DiagnosticsResponse> nullDiagnosticResponse = filter(having(on(DiagnosticsResponse.class).getResult(), Matchers.equalTo(null)), diagnosticsResponses);

        assertEquals(3, TestClass.methodExecutionCount);
        assertTrue(testDiagnostics1Response.get(0).getResult().getStatus());
        assertFalse(testDiagnostics2Response.get(0).getResult().getStatus());
        assertTrue(nullDiagnosticResponse.isEmpty());
    }
}