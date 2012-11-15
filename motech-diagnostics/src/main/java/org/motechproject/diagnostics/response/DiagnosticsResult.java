package org.motechproject.diagnostics.response;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectMax;

public class DiagnosticsResult {

    private String name;
    private String value;
    private Status status;
    private List<DiagnosticsResult> results = new ArrayList<>();

    public DiagnosticsResult(String name, String value, Status status) {
        this.value = value;
        this.name = name;
        this.status = status;
    }

    public DiagnosticsResult(String name, List<DiagnosticsResult> results) {
        this.name = name;
        this.results = results;
        this.status = getStatus();
    }

    public Status getStatus() {
        if(results.isEmpty())
            return status;

        return new DiagnosticResults(results).status();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public List<DiagnosticsResult> getResults() {
        return results;
    }

    public DiagnosticsResult add(DiagnosticsResult diagnosticsResult){
        results.add(diagnosticsResult);
        return this;
    }

}
