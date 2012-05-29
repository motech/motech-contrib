package org.motechproject.retry.dao;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.retry.domain.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllRetries extends MotechBaseRepository<Retry> {

    @Autowired
    public AllRetries(@Qualifier("retryConnector") CouchDbConnector dbConnector) {
        super(Retry.class, dbConnector);
    }

    public void createRetry(Retry retry) {
        Retry existingRetry = getActiveRetryWithStartTime(retry.name(), retry.externalId(), retry.startTime());
        if (existingRetry == null) {
            add(retry);
        }
    }

    @View(name = "get_retry_for_externalId_name_startTime", map = "function(doc) { if(doc.type === 'Retry' && doc.retryStatus === 'ACTIVE') emit([doc.externalId, doc.name, doc.startTime], doc) }")
    private Retry getActiveRetryWithStartTime(String name, String externalId, DateTime startTime) {
        List<Retry> retries = queryView("get_retry_for_externalId_name_startTime", ComplexKey.of(externalId, name, startTime));
        return retries.isEmpty() ? null : retries.get(0);
    }

    @View(name = "get_retry_for_externalId_name", map = "function(doc) { if(doc.type === 'Retry' && doc.retryStatus === 'ACTIVE') emit([doc.externalId, doc.name], doc) }")
    public Retry getActiveRetry(String externalId, String name) {
        List<Retry> retries = queryView("get_retry_for_externalId_name", ComplexKey.of(externalId, name));
        return retries.isEmpty() ? null : retries.get(0);
    }
}
