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
@ContextConfiguration("classpath:applicationContext-Diagnostics.xml")
public class PostgresDiagnosticIT {

    @Autowired
    private PostGresDiagnosticStub postGresDiagnosticStub;

    @Test
    public void shouldReturnSuccessfulMessageWhenPostgresConnectionIsSuccessful() {
        postGresDiagnosticStub.setConnectionIsSuccessful(true);
        DiagnosticsResult diagnosticsResult = postGresDiagnosticStub.performDiagnosis();
        System.out.println(diagnosticsResult.toString());
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getMessage().contains("Opening session Successful"));
    }

    @Test
    public void shouldReturnErrorMessageWhenPostgresConnectionFailed() {
        postGresDiagnosticStub.setConnectionIsSuccessful(false);
        DiagnosticsResult diagnosticsResult = postGresDiagnosticStub.performDiagnosis();
        System.out.println(diagnosticsResult.toString());
        assertNotNull(diagnosticsResult);
        assertTrue(diagnosticsResult.getMessage().contains("Opening session Failed"));
    }
}

@Component
class PostGresDiagnosticStub extends PostgresDiagnostic{


    private boolean shouldBeSuccessful;

    @Autowired
    public PostGresDiagnosticStub(@Qualifier("postgresProperties") Properties postgresProperties) {
        super(postgresProperties);
    }

    public void setConnectionIsSuccessful(boolean shouldBeSuccessful){

        this.shouldBeSuccessful = shouldBeSuccessful;
    }
    protected Connection getConnection() throws SQLException {
        if(shouldBeSuccessful)
            return null;
        throw new SQLException("Connection failed");
    }
}
