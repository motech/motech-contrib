package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.OutboundEventGateway;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.dao.AllRetriesDefinition;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.retry.EventKeys.*;

@Component
public class RetryHandler {
    private AllRetries allRetries;
    private AllRetriesDefinition allRetriesDefinition;
    private OutboundEventGateway outboundEventGateway;
    private RetryServiceImpl retryServiceImpl;

    @Autowired
    public RetryHandler(AllRetries allRetries, AllRetriesDefinition allRetriesDefinition, OutboundEventGateway outboundEventGateway, RetryServiceImpl retryServiceImpl) {
        this.allRetries = allRetries;
        this.allRetriesDefinition = allRetriesDefinition;
        this.outboundEventGateway = outboundEventGateway;
        this.retryServiceImpl = retryServiceImpl;
    }

    @MotechListener(subjects = RetryServiceImpl.RETRY_INTERNAL_SUBJECT)
    public void handle(MotechEvent event) {
        String externalId = (String) event.getParameters().get(EXTERNAL_ID);
        DateTime referenceTime = (DateTime) event.getParameters().get(REFERENCE_TIME);
        String retryRecordName = (String) event.getParameters().get(NAME);


        final Retry retry = decrementPendingRetryCount(externalId, retryRecordName);
        boolean lastRetryWithinCurrentGroup = !retry.hasPendingRetires();

        boolean lastRetryBatch = false;
        if (lastRetryWithinCurrentGroup) {
             lastRetryBatch = retryServiceImpl.scheduleNextGroup(new RetryRequest(retryRecordName, externalId, referenceTime));
        }
        String retryEventSubject = allRetriesDefinition.getRetryGroup(retryRecordName).getEventSubject();
        outboundEventGateway.sendEventMessage(motechEvent(retryEventSubject, event.getParameters(), lastRetryBatch));
    }

    private Retry decrementPendingRetryCount(String externalId, String retryRecordName) {
        Retry activeRetry = allRetries.getActiveRetry(externalId, retryRecordName);
        activeRetry.decrementRetriesLeft();
        if(!activeRetry.hasPendingRetires())
            activeRetry.setRetryStatus(RetryStatus.DEFAULTED);
        allRetries.update(activeRetry);
        return activeRetry;
    }

    private MotechEvent motechEvent(String retrySubject, Map<String, Object> parameters, boolean lastEvent) {
        MotechEvent motechEvent = new MotechEvent(retrySubject, parameters);
        motechEvent.setLastEvent(lastEvent);
        return motechEvent;
    }
}
