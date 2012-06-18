package org.motechproject.http.client.components;

import org.motechproject.http.client.listener.HttpClientEventListener;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.scheduler.event.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("asynchronous")
public class AsynchronousCall implements CommunicationType {

    private EventRelay eventRelay;

    private HttpClientEventListener httpClientEventListener;


    @Autowired
    public AsynchronousCall(EventRelay eventRelay, HttpClientEventListener httpClientEventListener) {
        this.eventRelay = eventRelay;
        this.httpClientEventListener = httpClientEventListener;
    }

    public void send(MotechEvent motechEvent) {
        eventRelay.sendEventMessage(motechEvent);
    }
}
