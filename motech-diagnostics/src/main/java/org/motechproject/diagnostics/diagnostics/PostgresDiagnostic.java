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

import static org.hibernate.exception.ExceptionUtils.getFullStackTrace;

@Component
public class PostgresDiagnostic {

    private Properties postgresProperties;

    @Autowired(required = false)
    public void setPostgresProperties(@Qualifier("postgresProperties") Properties postgresProperties) {
        this.postgresProperties = postgresProperties;
    }

    @Diagnostic(name = "POSTGRES DATABASE CONNECTION")
    public DiagnosticsResult<String> performDiagnosis() {
        if (postgresProperties == null) return null;
        Connection connection = null;
        try {
            connection = getConnection();
            if(connection != null)
                connection.close();
        } catch (SQLException e) {
            return new DiagnosticsResult<String>("POSTGRES DATABASE CONNECTION EXCEPTION", getFullStackTrace(e));
        }
        return new DiagnosticsResult<String>("POSTGRES DATABASE CONNECTION OPENED", "true");
    }

    protected Connection getConnection() throws SQLException {
        String url = postgresProperties.getProperty("jdbc.url");
        String userName = postgresProperties.getProperty("jdbc.username");
        String password = postgresProperties.getProperty("jdbc.password");
        return DriverManager.getConnection(url, userName, password);
    }
}