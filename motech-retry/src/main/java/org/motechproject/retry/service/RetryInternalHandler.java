package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;
import org.motechproject.server.event.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.motechproject.retry.EventKeys.*;

public class RetryInternalHandler {
    private AllRetries allRetries;
    private OutboundEventGateway outboundEventGateway;
    private RetryServiceImpl retryServiceImpl;

    @Autowired
    public RetryInternalHandler(AllRetries allRetries, OutboundEventGateway outboundEventGateway, RetryServiceImpl retryServiceImpl) {
        this.allRetries = allRetries;
        this.outboundEventGateway = outboundEventGateway;
        this.retryServiceImpl = retryServiceImpl;
    }

    @MotechListener(subjects = "org.motechproject.internal.retry")
    public void handle(MotechEvent event) {
        String externalId = (String) event.getParameters().get(EXTERNAL_ID);
        DateTime referenceTime = (DateTime) event.getParameters().get(REFERENCE_TIME);
        String retryRecordName = (String) event.getParameters().get(NAME);

        boolean isLastEvent = updateRetriesLeft(externalId, retryRecordName);
        if (isLastEvent) {
            retryServiceImpl.scheduleNext(new RetryRequest(retryRecordName, externalId, referenceTime, referenceTime));
        }

        outboundEventGateway.sendEventMessage(motechEvent(RETRY_SUBJECT, event.getParameters(), isLastEvent));
    }

    private boolean updateRetriesLeft(String externalId, String retryRecordName) {
        Retry activeRetry = allRetries.getActiveRetry(externalId, retryRecordName);
        activeRetry.decrementRetriesLeft();

        boolean isLastEvent = !activeRetry.hasRetriesLeft();
        if (isLastEvent) activeRetry.setRetryStatus(RetryStatus.DEFAULTED);
        allRetries.update(activeRetry);
        return isLastEvent;
    }

    private MotechEvent motechEvent(String retrySubject, Map<String, Object> parameters, boolean lastEvent) {
        MotechEvent motechEvent = new MotechEvent(retrySubject, parameters);
        motechEvent.setLastEvent(lastEvent);
        return motechEvent;
    }
}
