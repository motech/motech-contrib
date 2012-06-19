package org.ei.commcare.api.domain;

import java.util.Map;

public class CaseInformation {
    private Status status;
    private Map<String, String> properties;

    public CaseInformation(Status status, Map<String, String> properties) {
        this.status = status;
        this.properties = properties;
    }

    public Map<String, String> properties() {
        return properties;
    }

    public boolean isValid() {
        return status.equals(Status.SUCCESS);
    }

    public static enum Status {
        SUCCESS, FAILED
    }
}
