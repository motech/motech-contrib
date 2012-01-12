package it;

import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.motechproject.sms.smpp.constants.EventKeys;
import org.motechproject.sms.smpp.constants.EventSubject;

public class ReceiveSmsIT {

    @MotechListener(subjects = EventSubject.INBOUND_SMS)
    public void handle(MotechEvent event) {
        System.out.println(event.getParameters().get(EventKeys.INBOUND_MESSAGE));
        System.out.println(event.getParameters().get(EventKeys.SENDER));
    }
}