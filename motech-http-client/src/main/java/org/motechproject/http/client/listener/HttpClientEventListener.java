package org.motechproject.http.client.listener;


import org.apache.log4j.Logger;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
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
        Map<String,String> headers = (Map<String, String>) parameters.get(EventDataKeys.HEADERS);

        logger.info(String.format("Posting Http request -- Url: %s, Data: %s, Headers: %s", url, String.valueOf(requestData), headers));
        executeFor(url, requestData, method, headers);
    }

    private void executeFor(String url, Object requestData, Method method, Map<String, String> headers) {
        method.execute(restTemplate, url, requestData, headers);
    }
}
