package org.motechproject.diagnostics.diagnostics;

import org.ektorp.http.HttpClient;
import org.ektorp.http.HttpResponse;
import org.ektorp.impl.StdCouchDbConnector;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.diagnostics.response.Status.*;

@Component
public class CouchDBDiagnostics implements Diagnostics {

    @Autowired(required = false)
    private List<StdCouchDbConnector> allConnectors;

    public CouchDBDiagnostics() {
    }

    public CouchDBDiagnostics(List<StdCouchDbConnector> allConnectors) {
        this.allConnectors = allConnectors;
    }

    @Diagnostic(name = "IS_ACTIVE")
    public DiagnosticsResult isActive() {
        List<DiagnosticsResult> results = new ArrayList<>();
        for (StdCouchDbConnector connector : allConnectors) {
            results.add(isInstanceActive(connector));
        }
        return new DiagnosticsResult("couchdb is active", results);
    }

    private DiagnosticsResult isInstanceActive(StdCouchDbConnector connector) {
        try {
            HttpClient connection = connector.getConnection();
            HttpResponse httpResponse = connection.get("/");
            if (400 > httpResponse.getCode()) {
                return new DiagnosticsResult("Able to connect to " + connector.getDatabaseName(), "true", Success);
            } else {
                return new DiagnosticsResult("Able to connect to " + connector.getDatabaseName(), "false", Success);
            }
        } catch (Exception e) {
            return new DiagnosticsResult("Connecting to " + connector.getDatabaseName() + "resulted in", "error", Fail);
        }
    }

    @Override
    public String name() {
        return DiagnosticServiceName.COUCHDB;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return allConnectors != null && !allConnectors.isEmpty();

    }
}
