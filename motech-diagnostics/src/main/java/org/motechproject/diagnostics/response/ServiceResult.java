package org.motechproject.diagnostics.response;

import java.util.List;

public class ServiceResult {
    private String serviceName;
    private List<DiagnosticsResult> results;

    public ServiceResult(String serviceName, List<DiagnosticsResult> results) {
        this.serviceName = serviceName;
        this.results = results;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<DiagnosticsResult> getResults() {
        return results;
    }
}
