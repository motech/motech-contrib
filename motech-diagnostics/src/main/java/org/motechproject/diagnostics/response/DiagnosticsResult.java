package org.motechproject.diagnostics.response;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiagnosticsResult)) return false;

        DiagnosticsResult that = (DiagnosticsResult) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (results != null ? !results.equals(that.results) : that.results != null) return false;
        if (status != that.status) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }
}
