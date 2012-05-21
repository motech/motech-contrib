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

    private RestTemplate restTemplate=new RestTemplate();
    private int numberOfTries=0;
    private final int maxNumberOfTries = 20;

    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        numberOfTries++;
        if (numberOfTries <= maxNumberOfTries){
            Map<String,Object> parameters = motechEvent.getParameters();
            restTemplate.postForLocation(String.valueOf(parameters.get(EventDataKeys.URL)),parameters.get(EventDataKeys.DATA));
        }
    }

    public boolean hasTriedNumberOfTimes(int count) {
        return numberOfTries >= count;
    }
}
