package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.model.BatchJob;
import org.motechproject.diagnostics.repository.AllBatchJobs;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchJobDiagnostic implements Diagnostics {

    private AllBatchJobs allBatchJobs;

    @Autowired
    public BatchJobDiagnostic(AllBatchJobs allBatchJobs) {
        this.allBatchJobs = allBatchJobs;
    }

    @Diagnostic(name = "Batch job diagnostics")
    public DiagnosticsResult<List<DiagnosticsResult<String>>> performDiagnosis() {
        List<DiagnosticsResult<String>> results = new ArrayList<DiagnosticsResult<String>>();
        try {
            for (BatchJob batchJob : allBatchJobs.fetchAll()) {
                String value = batchJob.lastExecutionFailed() ? "false" : "true";
                results.add(new DiagnosticsResult<>(batchJob.getName(), value));
            }
        } catch (Exception e) {
            results.add(new DiagnosticsResult<>("Connecting to batch", "false"));
        }

        return new DiagnosticsResult<>("Batch Job", results);
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return allBatchJobs.canFetchData();
    }
}
