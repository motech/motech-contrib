package org.motechproject.diagnostics.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchJob {

    private String name;
    private JobInstances jobInstances;

    public String getName() {
        return name;
    }

    public BatchJob setName(String name) {
        this.name = name;
        return this;
    }

    public JobInstances getJobInstances() {
        return jobInstances;
    }

    public BatchJob setJobInstances(JobInstances jobInstances) {
        this.jobInstances = jobInstances;
        return this;
    }

    public boolean lastExecutionFailed() {
        if (null == jobInstances || jobInstances.isEmpty())
            return false;
        else
            return jobInstances.lastExecution().isFailure();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JobInstances extends HashMap<String, JobInstance> {

        public JobInstance lastExecution() {
            ArrayList<String> keys = new ArrayList<String>(this.keySet());
            if (keys.isEmpty()) {
                return null;
            } else {
                return this.get(Collections.max(keys));
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JobInstance {

        private String lastJobExecutionStatus;

        public boolean isFailure() {
            return "FAILED".equalsIgnoreCase(lastJobExecutionStatus);
        }

        public JobInstance setLastJobExecutionStatus(String lastJobExecutionStatus) {
            this.lastJobExecutionStatus = lastJobExecutionStatus;
            return this;
        }
    }
}
