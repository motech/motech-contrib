package org.motechproject.retry;

import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryStatus;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class RetryInternalHandler {
    private AllRetries allRetries;
    private OutboundEventGateway outboundEventGateway;

    @Autowired
    public RetryInternalHandler(AllRetries allRetries, OutboundEventGateway outboundEventGateway) {
        this.allRetries = allRetries;
        this.outboundEventGateway = outboundEventGateway;
    }

    @MotechListener(subjects = "org.motechproject.internal.retry")
    public void handle(MotechEvent event) {
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID);
        String retryRecordName = (String) event.getParameters().get(EventKeys.NAME);

        boolean isLastEvent = updateRetriesLeft(externalId, retryRecordName);

        outboundEventGateway.sendEventMessage(motechEvent(EventKeys.RETRY_SUBJECT, event.getParameters(), isLastEvent));
    }

    private boolean updateRetriesLeft(String externalId, String retryRecordName) {
        Retry activeRetry = allRetries.getActiveRetry(externalId, retryRecordName);
        activeRetry.decrementRetriesLeft();

        boolean isLastEvent = !activeRetry.hasRetriesLeft();
        if (isLastEvent) activeRetry.setRetryStatus(RetryStatus.INACTIVE);
        allRetries.update(activeRetry);
        return isLastEvent;
    }

    private MotechEvent motechEvent(String retrySubject, Map<String, Object> parameters, boolean lastEvent) {
        MotechEvent motechEvent = new MotechEvent(retrySubject, parameters);
        motechEvent.setLastEvent(lastEvent);
        return motechEvent;
    }
}
