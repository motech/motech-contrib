package org.motechproject.http.client.components;


import org.motechproject.model.MotechEvent;

public interface CommunicationType {

    public void send(MotechEvent motechEvent);
}
