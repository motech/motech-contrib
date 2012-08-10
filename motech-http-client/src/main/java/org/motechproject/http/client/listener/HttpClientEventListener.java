package org.motechproject.http.client.listener;


import org.apache.log4j.Logger;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClientEventListener {

    private RestTemplate restTemplate;
    Logger logger = Logger.getLogger(HttpClientEventListener.class);

    @Autowired
    public HttpClientEventListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        Map<String, Object> parameters = motechEvent.getParameters();
        String url = String.valueOf(parameters.get(EventDataKeys.URL));
        Object requestData = parameters.get(EventDataKeys.DATA);
        Method method = (Method) parameters.get(EventDataKeys.METHOD);
        logger.info(String.format("Posting Http request -- Url: %s, Data: %s", url, String.valueOf(requestData)));
        executeFor(url, requestData, method);
    }

    private void executeFor(String url, Object requestData, Method method) {
        method.execute(restTemplate, url, requestData);
    }
}
