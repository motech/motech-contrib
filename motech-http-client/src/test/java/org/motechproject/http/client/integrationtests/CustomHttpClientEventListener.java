package org.motechproject.http.client.integrationtests;


import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CustomHttpClientEventListener {

    private RestTemplate restTemplate;
    private int numberOfRetries;
    private final int maxNumberOfRetries = 2;

    public CustomHttpClientEventListener() {
        numberOfRetries = -1;
        restTemplate = new RestTemplate();
    }

    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        numberOfRetries++;
        if (numberOfRetries < maxNumberOfRetries){
        Map<String,Object> parameters = motechEvent.getParameters();
        restTemplate.postForLocation(String.valueOf(parameters.get(EventDataKeys.URL)),parameters.get(EventDataKeys.DATA));
        }
    }

    public boolean hasTriedMultipleTimes() {
        return numberOfRetries > 0;
    }
}
