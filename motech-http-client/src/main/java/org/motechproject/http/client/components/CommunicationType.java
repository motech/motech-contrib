package org.motechproject.http.client.components;

import org.motechproject.scheduler.domain.MotechEvent;

public interface CommunicationType {

    public void send(MotechEvent motechEvent);
}
