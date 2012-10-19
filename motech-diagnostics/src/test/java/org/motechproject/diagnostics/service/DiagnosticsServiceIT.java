package org.motechproject.diagnostics.service;


import org.ektorp.CouchDbConnector;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.util.TestClass;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationContext-DiagnosticsTest.xml")
public class DiagnosticsServiceIT extends SpringIntegrationTest {

    @Before
    @After
    public void setUpAndTearDown() {
        TestClass.methodExecutionCount = 0;
    }

    @Autowired
    private DiagnosticsService diagnosticService;

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }

    @Test
    public void shouldInvokeAllDiagnosticMethods() throws InvocationTargetException, IllegalAccessException {
        List<DiagnosticsResult> diagnosticsResponses = diagnosticService.runAll();

        List<DiagnosticsResult> testDiagnostics1Response = filter(having(on(DiagnosticsResult.class).getName(), Matchers.equalTo("test message 1")), diagnosticsResponses);
        List<DiagnosticsResult> testDiagnostics2Response = filter(having(on(DiagnosticsResult.class).getName(), Matchers.equalTo("test message 2")), diagnosticsResponses);

        assertEquals(3, TestClass.methodExecutionCount);
        assertTrue((Boolean) testDiagnostics1Response.get(0).getValue());
        assertFalse((Boolean) testDiagnostics2Response.get(0).getValue());
    }
}