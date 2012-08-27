package org.motechproject.diagnostics.diagnostics;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.HttpResponse;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.controller.DiagnosticServiceName;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CouchDBDiagnostics implements Diagnostics {

    private List<CouchDbInstance> allCouchDBInstances;

    @Autowired
    public CouchDBDiagnostics(List<CouchDbInstance> allCouchDBInstances) {
        this.allCouchDBInstances = allCouchDBInstances;
    }

    @Diagnostic(name = "IS_ACTIVE")
    public DiagnosticsResult<List<DiagnosticsResult>> isActive() {
        List<DiagnosticsResult> results = new ArrayList<DiagnosticsResult>();
        for (CouchDbInstance instance : allCouchDBInstances) {
            results.add(isInstanceActive(instance));
        }
        return new DiagnosticsResult<List<DiagnosticsResult>>("couchdb is active", results);
    }

    private DiagnosticsResult isInstanceActive(CouchDbInstance instance) {
        try {
            HttpClient connection = instance.getConnection();
            HttpResponse httpResponse = connection.get("/");
            if (400 > httpResponse.getCode()) {
                return new DiagnosticsResult("Able to connect to " + httpResponse.getRequestURI(), "true");
            } else {
                return new DiagnosticsResult("Able to connect to " + httpResponse.getRequestURI(), "false");
            }
        } catch (Exception e) {
            return new DiagnosticsResult("Able to connect to couchdb instance", "error");
        }
    }

    @Override
    public String name() {
        return DiagnosticServiceName.COUCHDB;
    }
}
