package org.motechproject.http.client.domain;

import org.motechproject.event.MotechEvent;

import java.util.Map;

public class EventCallBack {
    private String subject;
    private Map<String, Object> parameters;

    public EventCallBack(String subject, Map<String, Object> parameters) {
        this.subject = subject;
        this.parameters = parameters;
    }

    public MotechEvent getCallBackEvent(){
        return new MotechEvent(subject, parameters);
    }
}
