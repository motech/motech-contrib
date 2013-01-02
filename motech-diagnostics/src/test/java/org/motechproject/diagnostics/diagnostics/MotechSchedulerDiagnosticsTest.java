package org.motechproject.diagnostics.diagnostics;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.diagnostics.response.Status.Fail;
import static org.motechproject.diagnostics.response.Status.Success;

public class MotechSchedulerDiagnosticsTest {

    @Mock
    private Scheduler motechScheduler;
    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;
    @Mock
    private DiagnosticConfiguration diagnosticConfiguration;

    private final String schedulerDiagnosticsFormat = "Job : %s\nPrevious Fire Time : %s\nNext Fire Time : %s\nHas job run in previous day : %s";
    private MotechSchedulerDiagnostics motechSchedulerDiagnostics;

    @Before
    public void setUp() {
        initMocks(this);
        when(schedulerFactoryBean.getScheduler()).thenReturn(motechScheduler);
        motechSchedulerDiagnostics = new MotechSchedulerDiagnostics();
        motechSchedulerDiagnostics.setDiagnosticConfiguration(diagnosticConfiguration);
        motechSchedulerDiagnostics.setMotechScheduler(motechScheduler);
    }

    @Test
    public void shouldCheckIfSchedulerIsRunning() throws SchedulerException {
        when(motechScheduler.isStarted()).thenReturn(false);
        DiagnosticsResult expectedResultWhenSchedulerNotRunning = new DiagnosticsResult("Motech Scheduler", "Not Running", Fail);
        assertEquals(expectedResultWhenSchedulerNotRunning, motechSchedulerDiagnostics.isSchedulerRunning());

        when(motechScheduler.isStarted()).thenReturn(true);
        DiagnosticsResult expectedResultWhenSchedulerIsRunning = new DiagnosticsResult("Motech Scheduler", "Running", Success);
        assertEquals(expectedResultWhenSchedulerIsRunning, motechSchedulerDiagnostics.isSchedulerRunning());
    }

    @Test
    public void shouldReturnJobStatuses() throws SchedulerException {
        Set<TriggerKey> triggerKeys = new HashSet<>();
        TriggerKey triggerKey = new TriggerKey("name1", "default");
        triggerKeys.add(triggerKey);
        when(motechScheduler.getTriggerKeys(GroupMatcher.triggerGroupContains(anyString()))).thenReturn(triggerKeys);

        Trigger mockTrigger = mock(Trigger.class);
        when(motechScheduler.getTrigger(triggerKey)).thenReturn(mockTrigger);
        final String jobName1 = "Job Name1";
        when(mockTrigger.getJobKey()).thenReturn(new JobKey(jobName1));
        when(motechScheduler.isStarted()).thenReturn(true);

        ArrayList jobKeyList = mock(ArrayList.class);
        when(motechScheduler.getTriggersOfJob(any(JobKey.class))).thenReturn(jobKeyList);
        when(jobKeyList.size()).thenReturn(2);

        when(mockTrigger.getPreviousFireTime()).thenReturn(null);
        Date nextFireTime = DateTime.now().plusMonths(4).toDate();
        when(mockTrigger.getNextFireTime()).thenReturn(nextFireTime);

        when(diagnosticConfiguration.scheduleJobNames()).thenReturn(asList(jobName1));

        DiagnosticsResult diagnosticsResult = motechSchedulerDiagnostics.jobStatuses();

        assertEquals(Status.Success, diagnosticsResult.getStatus());
        assertEquals(jobName1 + " - " + "Previous Fire Time", diagnosticsResult.getResults().get(0).getName());
        assertNull(diagnosticsResult.getResults().get(0).getValue());

        assertEquals(jobName1 + " - " + "Next Fire Time", diagnosticsResult.getResults().get(1).getName());
        assertEquals(nextFireTime.toString(), diagnosticsResult.getResults().get(1).getValue());
    }

    @Test
    public void shouldNotPerformDiagnosticsIfJobNamesIsEmpty() {
        motechSchedulerDiagnostics.setMotechScheduler(motechScheduler);

        when(diagnosticConfiguration.scheduleJobNames()).thenReturn(new ArrayList<String>());
        assertFalse(motechSchedulerDiagnostics.canPerformDiagnostics());

        when(diagnosticConfiguration.scheduleJobNames()).thenReturn(asList("job1"));
        assertTrue(motechSchedulerDiagnostics.canPerformDiagnostics());
    }

    @Test
    public void shouldNotPerformDiagnosticsIfSchedulerIsConfigured() {
        when(diagnosticConfiguration.scheduleJobNames()).thenReturn(asList("job1"));
        motechSchedulerDiagnostics.setMotechScheduler(null);
        assertFalse(motechSchedulerDiagnostics.canPerformDiagnostics());

        motechSchedulerDiagnostics.setMotechScheduler(motechScheduler);
        assertTrue(motechSchedulerDiagnostics.canPerformDiagnostics());
    }
}
