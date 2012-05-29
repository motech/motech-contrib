package org.motechproject.retry.service;

import org.motechproject.retry.domain.RetryRequest;

public interface RetryService {
    void schedule(RetryRequest retryRequest);

    void unscheduleRetryGroup(String externalId, String groupName);

    void fulfill(String externalId, String groupName);

}
