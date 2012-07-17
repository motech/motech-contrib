package org.motechproject.retry.service;

import org.motechproject.retry.domain.RetryRequest;

public interface RetryService {
    public static final String RETRY_INTERNAL_SUBJECT = "org.motechproject.retry.internal";

    void schedule(RetryRequest retryRequest);

    void unscheduleRetryGroup(String externalId, String groupName);

    void fulfill(String externalId, String groupName);

}
