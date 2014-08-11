package org.motechproject.casexml.gateway;

import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.components.CommunicationType;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.components.SynchronousCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommcareCaseGateway {

    private CaseTaskXmlConverter caseTaskXmlConverter;
    private CommunicationType communicationType;

    @Autowired
    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter, SynchronousCall synchronousCall) {
        this.caseTaskXmlConverter = caseTaskXmlConverter;
        this.communicationType = synchronousCall;
    }

    public void submitCase(String commcareUrl, CaseTask task, String username, String password, Integer redeliveryCount) {
        String request = caseTaskXmlConverter.convertToCaseXml(task);
        HashMap<String, Object> parameters = constructParametersFrom(commcareUrl, request, "POST", username, password);
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, parameters);
        sendEventMessage(motechEvent, redeliveryCount);
    }

    public void closeCase(String commcareUrl, CaseTask task, String username, String password, Integer redeliveryCount) {
        String request = caseTaskXmlConverter.convertToCloseCaseXml(task);
        HashMap<String, Object> parameters = constructParametersFrom(commcareUrl, request, "POST", username, password);
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, parameters);
        sendEventMessage(motechEvent, redeliveryCount);
    }

    private void sendEventMessage(MotechEvent event, Integer redeliveryCount) {
        try {
            communicationType.send(event);
        } catch (Exception e) {
            event.getParameters().put(MotechEvent.PARAM_INVALID_MOTECH_EVENT, Boolean.TRUE);
            if (event.getMessageRedeliveryCount() == redeliveryCount || redeliveryCount == null) {
                event.getParameters().put(MotechEvent.PARAM_DISCARDED_MOTECH_EVENT, Boolean.TRUE);
                // max retry count reached
                throw e;
            }
            event.incrementMessageRedeliveryCount();
            sendEventMessage(event, redeliveryCount);
        }
    }

    private HashMap<String, Object> constructParametersFrom(String url, Object data, String method, String username, String password) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.METHOD, method);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.USERNAME, username);
        parameters.put(EventDataKeys.PASSWORD, password);
        return parameters;
    }
}
