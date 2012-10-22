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
    public DiagnosticsResult<List<DiagnosticsResult>> isActive() {
        List<DiagnosticsResult> results = new ArrayList<DiagnosticsResult>();
        for (StdCouchDbConnector connector : allConnectors) {
            results.add(isInstanceActive(connector));
        }
        return new DiagnosticsResult<>("couchdb is active", results);
    }

    private DiagnosticsResult isInstanceActive(StdCouchDbConnector connector) {
        try {
            HttpClient connection = connector.getConnection();
            HttpResponse httpResponse = connection.get("/");
            if (400 > httpResponse.getCode()) {
                return new DiagnosticsResult("Able to connect to " + connector.getDatabaseName(), "true");
            } else {
                return new DiagnosticsResult("Able to connect to " + connector.getDatabaseName(), "false");
            }
        } catch (Exception e) {
            return new DiagnosticsResult("Connecting to " + connector.getDatabaseName() + "resulted in", "error");
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
