package org.motechproject.diagnostics.diagnostics.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.diagnostics.PostgresDiagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
@DirtiesContext
public class PostgresDiagnosticIT {

    @Autowired
    private PostGresDiagnosticStub postGresDiagnosticStub;

    @Test
    public void shouldReturnSuccessfulMessageWhenPostgresConnectionIsSuccessful() {
        postGresDiagnosticStub.setConnectionIsSuccessful(true);
        DiagnosticsResult diagnosticsResult = postGresDiagnosticStub.performDiagnosis();
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getName().contains("POSTGRES DATABASE CONNECTION OPENED"));
    }

    @Test
    public void shouldReturnErrorMessageWhenPostgresConnectionFailed() {
        postGresDiagnosticStub.setConnectionIsSuccessful(false);
        DiagnosticsResult diagnosticsResult = postGresDiagnosticStub.performDiagnosis();
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getName().contains("POSTGRES DATABASE CONNECTION EXCEPTION"));
    }
}

@Component
class PostGresDiagnosticStub extends PostgresDiagnostic {

    private boolean shouldBeSuccessful;

    public void setConnectionIsSuccessful(boolean shouldBeSuccessful) {

        this.shouldBeSuccessful = shouldBeSuccessful;
    }

    protected Connection getConnection() throws SQLException {
        if (shouldBeSuccessful)
            return null;
        throw new SQLException("Connection failed");
    }
}