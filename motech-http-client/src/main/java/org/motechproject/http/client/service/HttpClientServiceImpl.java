package org.motechproject.http.client.service;


import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.components.SynchronousCall;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class HttpClientServiceImpl implements HttpClientService {

    @Resource(name = "${communication.type}")
    private CommunicationType communicationType;

    @Autowired
    private SynchronousCall synchronousCall;

    @Override
    public void post(String url, Object data) {
        execute(url, data, Method.POST, null);
    }

    @Override
    public void post(String url, Object data, Map<String, String> headers) {
        execute(url, data, Method.POST, headers);
    }

    @Override
    public void put(String url, Object data) {
        execute(url, data, Method.PUT, null);
    }

    @Override
    public void put(String url, Object data, Map<String, String> headers) {
        execute(url, data, Method.PUT, headers);
    }

    @Override
    public void execute(String url, Object data, Method method) {
        execute(url, data, method, null);
    }

    @Override
    public void execute(String url, Object data, Method method, Map<String, String> headers) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, headers, method);
        sendMessage(parameters);
    }

    @Override
    public void executeSync(String url, Object data, Method method) {
        executeSync(url, data, method, null);
    }

    @Override
    public void executeSync(String url, Object data, Method method, Map<String, String> headers) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, headers, method);
        sendMessageSync(parameters);
    }

    private HashMap<String, Object> constructParametersFrom(String url, Object data, Map<String, String> headers, Method method) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.METHOD, method);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.HEADERS, headers);
        return parameters;
    }

    private void sendMessage(HashMap<String, Object> parameters) {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, parameters);
        communicationType.send(motechEvent);
    }

    private void sendMessageSync(HashMap<String, Object> parameters) {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, parameters);
        synchronousCall.send(motechEvent);
    }
}
