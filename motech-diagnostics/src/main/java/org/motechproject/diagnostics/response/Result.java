package org.motechproject.diagnostics.response;

import java.util.List;

public class Result {

    private String serviceName;
    private List<DiagnosticsResult> diagnosticsResults;

    public Result(String serviceName, List<DiagnosticsResult> diagnosticsResults) {
        this.serviceName = serviceName;
        this.diagnosticsResults = diagnosticsResults;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<DiagnosticsResult> getDiagnosticsResults() {
        return diagnosticsResults;
    }
}
