package org.motechproject.http.client.service;


import org.motechproject.event.EventRelay;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.motechproject.model.MotechEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class HttpClientServiceImpl implements HttpClientService {

    private EventRelay eventRelay;

    @Autowired
    public HttpClientServiceImpl(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @Override
    public void post(String url, String data) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.HTTP_REQUEST, parameters));
    }
}
