package org.motechproject.http.client.listener;


import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClientEventListener {

    private RestTemplate restTemplate;
    Logger logger = Logger.getLogger(HttpClientEventListener.class);

    public HttpClientEventListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpClientEventListener() {
        this(new RestTemplate());
    }

    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        Map<String, Object> parameters = motechEvent.getParameters();
        String url = String.valueOf(parameters.get(EventDataKeys.URL));
        Object requestData = parameters.get(EventDataKeys.DATA);
        logger.info(String.format("Posting Http request -- Url: %s, Data: %s", url, String.valueOf(requestData)));
        restTemplate.postForLocation(url, requestData);
    }
}
