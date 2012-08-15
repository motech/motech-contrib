package org.motechproject.diagnostics.diagnostics;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ektorp.impl.StdCouchDbInstance;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class CouchDBDiagnostic {

    private Properties couchdbProperties;
    private StdCouchDbInstance dbInstance;
    private HashMap<String, String> diagnosticUrl;

    public CouchDBDiagnostic() {
    }

    @Autowired(required = false)
    public CouchDBDiagnostic(@Qualifier("couchdbProperties") Properties couchdbProperties, @Qualifier("dbInstance") StdCouchDbInstance dbInstance, HashMap<String, String> diagnosticUrl) {
        this.dbInstance = dbInstance;
        this.diagnosticUrl = diagnosticUrl;
        this.couchdbProperties = couchdbProperties;
    }

    @Diagnostic(name = "COUCH DATABASE CONNECTION")
    public DiagnosticsResult performDiagnosis() {
        if (couchdbProperties == null) return null;
        DiagnosticLog diagnosticLog = new DiagnosticLog();
        diagnosticLog.add("Checking couch db connection");
        Boolean status = true;
        try {
            dbInstance.getConnection().head("/");
            diagnosticLog.add("Databases present : " + dbInstance.getAllDatabases().toString());

            Map<String, Integer> results = collect();
            for (String result : results.keySet()) {
                Integer statusCode = results.get(result);
                diagnosticLog.add(result + " : HTTP Status Code: " + statusCode);
                if (statusCode != 200) status = false;
            }
        } catch (Exception e) {
            diagnosticLog.add("Couch DB connection failed");
            status = false;
            diagnosticLog.addError(e);
        }
        return new DiagnosticsResult(status, diagnosticLog.toString());
    }

    public Map<String, Integer> collect() throws IOException {
        HttpClient httpClient = new HttpClient();
        Map<String, Integer> results = new HashMap<>();
        for (Map.Entry<String, String> url : diagnosticUrl.entrySet()) {
            GetMethod method = new GetMethod(getFor(url.getValue()));
            Integer status = httpClient.executeMethod(method);
            results.put(url.getKey(), status);
        }
        return results;
    }

    private String getFor(String url) {
        return String.format(url,
                couchdbProperties.getProperty("host"),
                couchdbProperties.getProperty("port"));
    }
}