package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class PostgresDiagnostic {

    private Properties postgresProperties;

    @Autowired(required = false)
    public void setPostgresProperties(@Qualifier("postgresProperties")Properties postgresProperties) {
        this.postgresProperties = postgresProperties;
    }

    @Diagnostic(name = "postgres")
    public DiagnosticsResult performDiagnosis() {
        if(postgresProperties == null) return null;
        DiagnosticLog diagnosticLog = new DiagnosticLog("POSTGRES");
        diagnosticLog.add("Opening session with database");
        try {
            getConnection();
        } catch (SQLException e) {
            diagnosticLog.add("Opening session Failed");
            diagnosticLog.add(e.toString());
            return new DiagnosticsResult(false,diagnosticLog.toString());
        }
        diagnosticLog.add("Opening session Successful");
        return new DiagnosticsResult(true,diagnosticLog.toString());
    }

    protected Connection getConnection() throws SQLException {
        String url = postgresProperties.getProperty("jdbc.diagnosticCheck.url");
        String userName = postgresProperties.getProperty("jdbc.username");
        String password = postgresProperties.getProperty("jdbc.password");
        return DriverManager.getConnection(url, userName, password);
    }
}