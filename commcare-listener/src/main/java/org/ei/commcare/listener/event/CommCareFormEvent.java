package org.ei.commcare.listener.event;

import org.ei.commcare.api.domain.CommCareFormInstance;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.List;

public class CommCareFormEvent {
    public static final String EVENT_SUBJECT = "FORMS_FOR_MODULE_EVENT";
    public static final String FORM_INSTANCES_PARAMETER = "FormInstances";

    private List<CommCareFormInstance> formInstances;

    public CommCareFormEvent(List<CommCareFormInstance> formInstances) {
        this.formInstances = formInstances;
    }

    public MotechEvent toMotechEvent() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(FORM_INSTANCES_PARAMETER, formInstances);
        return new MotechEvent(EVENT_SUBJECT, parameters);
    }
}