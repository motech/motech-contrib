package org.motechproject.retry.utils;

import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private boolean eventFired;

    @MotechListener(subjects = "retry.every.second")
    public void handleEvent(MotechEvent motechEvent) {
        eventFired = true;
    }

    public boolean hasEventBeenFired() {
        return eventFired;
    }

    public void reset() {
        eventFired = false;
    }
}
