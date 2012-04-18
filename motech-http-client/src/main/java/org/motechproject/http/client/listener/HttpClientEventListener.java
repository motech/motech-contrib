package org.motechproject.http.client.listener;


import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClientEventListener {

    private RestTemplate restTemplate;

    public HttpClientEventListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public HttpClientEventListener() {
        this(new RestTemplate());
    }

    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        Map<String,Object> parameters = motechEvent.getParameters();
        restTemplate.postForLocation(String.valueOf(parameters.get(EventDataKeys.URL)),parameters.get(EventDataKeys.DATA));
    }
}
