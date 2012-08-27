package org.motechproject.diagnostics.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class PostgresDiagnosticIT {

    @Autowired
    private PostgresDiagnosticStub postgresDiagnosticStub;

    @Test
    public void shouldReturnSuccessfulMessageWhenPostgresConnectionIsSuccessful() throws SQLException {
        postgresDiagnosticStub.setConnectionIsSuccessful(true);
        DiagnosticsResult diagnosticsResult = postgresDiagnosticStub.performDiagnosis();
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getMessage().contains("Successfully opened a session."));
    }

    @Test
    public void shouldReturnErrorMessageWhenPostgresConnectionFailed() throws SQLException {
        postgresDiagnosticStub.setConnectionIsSuccessful(false);
        DiagnosticsResult diagnosticsResult = postgresDiagnosticStub.performDiagnosis();
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getMessage().contains("Could not open a session."));
    }
}

@Component
class PostgresDiagnosticStub extends PostgresDiagnostic {
    private boolean shouldBeSuccessful;

    @Autowired
    public PostgresDiagnosticStub(@Qualifier("postgresProperties") Properties postgresProperties) {
        super(postgresProperties);
    }

    public void setConnectionIsSuccessful(boolean shouldBeSuccessful) {
        this.shouldBeSuccessful = shouldBeSuccessful;
    }

    protected Connection getConnection(String url, String username, String password) throws SQLException {
        if (shouldBeSuccessful)
            return null;
        throw new SQLException("Connection failed");
    }
}