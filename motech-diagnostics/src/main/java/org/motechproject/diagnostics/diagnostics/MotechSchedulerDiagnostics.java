package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.motechproject.diagnostics.response.Status.Fail;
import static org.motechproject.diagnostics.response.Status.Success;

@Component
public class MotechSchedulerDiagnostics implements Diagnostics {

    @Autowired(required = false)
    private Scheduler motechScheduler;

    @Autowired
    private DiagnosticConfiguration diagnosticConfiguration;

    @Diagnostic(name = "Scheduler Job Status")
    public DiagnosticsResult jobStatuses() throws SchedulerException {
        List<DiagnosticsResult> results = getJobDetailsFor(diagnosticConfiguration.scheduleJobNames());
        return new DiagnosticsResult("Job Status", results);
    }

    @Diagnostic(name = "Is Scheduler Running?")
    public DiagnosticsResult isSchedulerRunning() throws SchedulerException {
        boolean schedulerRunning = motechScheduler.isStarted();
        return new DiagnosticsResult("Motech Scheduler",
                schedulerRunning ? "Running": "Not Running",
                schedulerRunning ?  Success : Fail);
    }

    private List<DiagnosticsResult> getJobDetailsFor(List<String> jobs) throws SchedulerException {
        List<TriggerKey> triggerKeys = new ArrayList<>(motechScheduler.getTriggerKeys(GroupMatcher.triggerGroupContains("default")));
        List<DiagnosticsResult> jobDetailsList = new ArrayList<>();
        for (TriggerKey triggerKey : triggerKeys) {
            Trigger trigger = motechScheduler.getTrigger(triggerKey);
            JobKey jobKey = trigger.getJobKey();
            if (motechScheduler.getTriggersOfJob(jobKey).size() > 0 && isForJob(jobKey.getName(), jobs)) {
                Date previousFireTime = trigger.getPreviousFireTime();
                Date nextFireTime = trigger.getNextFireTime();
                jobDetailsList.add(new DiagnosticsResult(jobKey.getName() + " - " + "Previous Fire Time", previousFireTime!= null ? previousFireTime.toString() : null, Success));
                jobDetailsList.add(new DiagnosticsResult(jobKey.getName() + " - " + "Next Fire Time", nextFireTime != null ? nextFireTime.toString() : null, Success));
            }
        }
        return jobDetailsList;
    }

    private boolean isForJob(String name, List<String> jobs) {
        for (String job : jobs) {
            if (name.contains(job))
                return true;
        }
        return false;
    }

    @Override
    public String name() {
        return DiagnosticServiceName.SCHEDULER;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return motechScheduler != null && !diagnosticConfiguration.scheduleJobNames().isEmpty();
    }

    public void setMotechScheduler(Scheduler motechScheduler) {
        this.motechScheduler = motechScheduler;
    }

    public void setDiagnosticConfiguration(DiagnosticConfiguration diagnosticConfiguration) {
        this.diagnosticConfiguration = diagnosticConfiguration;
    }
}
