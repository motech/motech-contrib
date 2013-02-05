package org.motechproject.diagnostics.diagnostics;

import org.joda.time.LocalDateTime;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.common.JdbcUtils;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.DBConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private static final String GET_JOB_SUMMARY = "SELECT job_group, trigger_state, count(1), next_fire_time FROM qrtz_triggers " +
            "group by next_fire_time,  job_group, trigger_state " +
            "order by job_group, next_fire_time, trigger_state";


    @Diagnostic(name = "Summary Of Jobs")
    public DiagnosticsResult jobSummaries() throws SchedulerException, SQLException {
        String quartzDataSourceName = diagnosticConfiguration.getQuartzDataSourceName();
        if(quartzDataSourceName == null){
            new DiagnosticsResult("Summary Of Jobs", "No Quartz Data Source Name defined.", Status.Unknown);
        }

        List<DiagnosticsResult> diagnosticsResults = new ArrayList<>();
        Connection connection = null;
        try{
            connection = DBConnectionManager.getInstance().getConnection(quartzDataSourceName);
            List<Map<String,Object>> queryResults = JdbcUtils.query(connection, GET_JOB_SUMMARY);
            for(Map<String, Object> result : queryResults){
                String jobGroup = (String) result.get("job_group");
                String triggerState = (String) result.get("trigger_state");
                String count = String.valueOf(result.get("count"));
                Long nextFireTime = (Long) result.get("next_fire_time");
                String diagnosticResultValue = String.format("Job Group: %s Trigger State: %s Next Fire Time: %s Count:", jobGroup, triggerState, getFormattedTime(nextFireTime));
                diagnosticsResults.add(new DiagnosticsResult(diagnosticResultValue, count, Status.Success));
            }
        } finally {
            connection.close();
        }

        return new DiagnosticsResult("Summary Of Jobs", diagnosticsResults);
    }

    private String getFormattedTime(Long nextFireTime) {
        String formattedNextFireTime;
        if(nextFireTime == null || nextFireTime == -1) {
            formattedNextFireTime = "NULL";
        } else {
            formattedNextFireTime = new LocalDateTime(nextFireTime).toString("yyyy-MM-dd HH:mm:ss");
        }
        return formattedNextFireTime;
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
