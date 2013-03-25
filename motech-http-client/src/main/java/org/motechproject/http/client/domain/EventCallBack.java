package org.motechproject.http.client.domain;

import org.motechproject.event.MotechEvent;

import java.io.Serializable;
import java.util.Map;

public class EventCallBack implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String subject;
    private Map<String, Object> parameters;

    public EventCallBack(String subject, Map<String, Object> parameters) {
        this.subject = subject;
        this.parameters = parameters;
    }

    public MotechEvent getCallBackEvent(){
        return new MotechEvent(subject, parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCallBack)) return false;

        EventCallBack that = (EventCallBack) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
