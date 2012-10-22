package org.motechproject.diagnostics.response;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectMax;

public class DiagnosticsResult {

    private String name;
    private String value;
    private List<DiagnosticsResult> results = new ArrayList<>();
    private Status status;

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

    private int statusLevel(){
        return status.level();
    }

    public Status getStatus() {
        int statusLevel = 0;
        for(DiagnosticsResult result : results){
            statusLevel = status(statusLevel, result.getStatus().level());
        }
        return Status.status(statusLevel);
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

    private int status(int oldStatus, int newStatus){
        if(oldStatus > newStatus)
            return oldStatus;
        else
            return newStatus;
    }

}
