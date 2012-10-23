package org.motechproject.diagnostics.response;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticResults extends ArrayList<DiagnosticsResult> {

    public DiagnosticResults(List<DiagnosticsResult> results) {
        addAll(results);
    }

    public Status status() {
        int statusLevel = 0;
        for(DiagnosticsResult result : this){
            statusLevel = status(statusLevel, result.getStatus().level());
        }
        return Status.status(statusLevel);
    }


    private int status(int oldStatus, int newStatus){
        if(oldStatus > newStatus)
            return oldStatus;
        else
            return newStatus;
    }

}
