package org.motechproject.http.client.service;


import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;

@Service
public class HttpClientServiceImpl implements HttpClientService {

    @Resource(name = "${communication.type}")
    private CommunicationType communicationType;

    @Override
    public void post(String url, Serializable data) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, Method.POST);
        parameters.put(EventDataKeys.HEADERS, new HashMap<String, String>());
        communicationType.send(createMotechEvent(parameters));
    }

    @Override
    public void post(String url, Serializable data, HashMap<String, String> headers) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, Method.POST);
        parameters.put(EventDataKeys.HEADERS, headers);
        communicationType.send(createMotechEvent(parameters));
    }


    @Override
    public void put(String url, Serializable data) {
        HashMap<String, Object> parameters = constructParametersFrom(url, data, Method.PUT);
        parameters.put(EventDataKeys.HEADERS, new HashMap<String, String>());
        communicationType.send(createMotechEvent(parameters));
    }

    private MotechEvent createMotechEvent(HashMap<String, Object> parameters) {
        return new MotechEvent(EventSubjects.HTTP_REQUEST, parameters);
    }

    private HashMap<String, Object> constructParametersFrom(String url, Serializable data, Method method) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.METHOD, method);
        parameters.put(EventDataKeys.DATA, data);
        return parameters;
    }
}
