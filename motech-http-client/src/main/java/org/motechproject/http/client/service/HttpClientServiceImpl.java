package org.motechproject.http.client.service;


import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.Method;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;

import static org.motechproject.http.client.domain.EventDataKeys.*;
import static org.motechproject.http.client.domain.EventSubjects.HTTP_REQUEST;
import static org.motechproject.http.client.domain.Method.POST;
import static org.motechproject.http.client.domain.Method.PUT;

@Service
public class HttpClientServiceImpl implements HttpClientService {

    @Resource(name = "${communication.type}")
    private CommunicationType communicationType;

    @Override
    public void post(String url, Serializable data) {
        createAndSendMotechEvent(url, data, emptyHeaders(), POST);
    }

    @Override
    public void post(String url, Serializable data, HashMap<String, String> headers) {
        createAndSendMotechEvent(url, data, headers, POST);
    }

    @Override
    public void post(String url, Serializable data, HashMap<String, String> headers, EventCallBack eventCallBack) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, headers, POST);
        parameters.put(EventDataKeys.CALLBACK, eventCallBack);
        sendMotechEvent(parameters);
    }

    @Override
    public void put(String url, Serializable data) {
        createAndSendMotechEvent(url, data, emptyHeaders(), PUT);
    }

    private void createAndSendMotechEvent(String url, Serializable data, HashMap<String, String> headers, Method method){
        HashMap<String, Object> parameters = constructParametersFrom(url, data, headers, method);
        sendMotechEvent(parameters);
    }

    private void sendMotechEvent(HashMap<String, Object> parameters) {
        MotechEvent motechEvent = new MotechEvent(HTTP_REQUEST, parameters);
        communicationType.send(motechEvent);
    }

    private HashMap<String, Object> constructParametersFrom(String url, Serializable data, HashMap<String, String> headers, Method method) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(URL, url);
        parameters.put(METHOD, method);
        parameters.put(DATA, data);
        parameters.put(HEADERS, headers);
        return parameters;
    }

    private HashMap<String, String> emptyHeaders() {
        return new HashMap<>();
    }
}
