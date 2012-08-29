package org.motechproject.diagnostics.diagnostics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.diagnostics.model.BatchJob;
import org.motechproject.diagnostics.repository.AllBatchJobs;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BatchJobDiagnosticTest {

    @Mock
    AllBatchJobs allBatchJobs;
    BatchJobDiagnostic batchJobDiagnostics;

    @Before
    public void setup() {
        initMocks(this);
        batchJobDiagnostics = new BatchJobDiagnostic(allBatchJobs);
    }

    @Test
    public void shouldPerformDiagnosticsUponInitialization() {
        assertTrue(batchJobDiagnostics.canPerformDiagnostics());
    }

    @Test
    public void shouldDiagnoseBatchJob() {
        BatchJob viewIndexerJob = createSpringBatchJob("viewIndexerJob", "COMPLETED", "COMPLETED", "FAILED");
        BatchJob dbCompactorJob = createSpringBatchJob("dbCompactorJob", "COMPLETED", "COMPLETED");

        when(allBatchJobs.fetchAll()).thenReturn(asList(viewIndexerJob, dbCompactorJob));

        DiagnosticsResult<List<DiagnosticsResult<String>>> results = batchJobDiagnostics.performDiagnosis();
        assertEquals("false", results.getValue().get(0).getValue());
        assertEquals("true", results.getValue().get(1).getValue());
    }

    @Test
    public void shouldDiagnoseAllTheJobsMentionedInTheProperties() {

    }

    @Test
    public void shouldDiagnoseJobWhenDiagnosisForPredecessorFails() {

    }

    private BatchJob createSpringBatchJob(String jobName, String... jobStatuses) {
        BatchJob.JobInstances viewIndexerJobInstances = new BatchJob.JobInstances();
        for (Integer i = 0; i< jobStatuses.length; i++) {
            viewIndexerJobInstances.put(i.toString(), new BatchJob.JobInstance().setLastJobExecutionStatus(jobStatuses[i]));
        }
        return new BatchJob().setName(jobName).setJobInstances(viewIndexerJobInstances);
    }
}
