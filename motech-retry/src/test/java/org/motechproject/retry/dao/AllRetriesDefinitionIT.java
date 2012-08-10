package org.motechproject.retry.dao;


import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.retry.domain.RetryGroupRecord;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class AllRetriesDefinitionIT {

    @Autowired
    private AllRetriesDefinition allRetriesDefinition;
    @Test
    public void shouldReadDefinitionFromJSON() {
        RetryGroupRecord retryGroup = allRetriesDefinition.getRetryGroup("retry-every-2hrs-and-30mins");
        assertEquals("campaign-retries", retryGroup.getName());
        assertEquals("campaign-retries-event", retryGroup.getEventSubject());
        List<RetryRecord> retries = retryGroup.getRetries();
        assertEquals(2, retries.size());

        RetryRecord retryRecord1 = retries.get(0);
        RetryRecord retryRecord2 = retries.get(1);

        assertEquals("retry-every-2hrs-and-30mins", retryRecord1.name());
        assertEquals(new Period().plusMinutes(30), retryRecord1.offset());
        assertEquals((Integer)5, retryRecord1.retryCount());
        assertEquals(new Period().plusHours(2).plusMinutes(30), retryRecord1.retryInterval());

        RetryRecord nextRetryRecord = allRetriesDefinition.getNextRetryRecord(retryRecord1.name());
        assertEquals(retryRecord2.name(), nextRetryRecord.name());
        assertEquals("retry-every-10Days", retryRecord2.name());
        assertEquals(new Period().plusMinutes(30), retryRecord2.offset());
        assertEquals((Integer)5, retryRecord2.retryCount());
        assertEquals(new Period().plusDays(10), retryRecord2.retryInterval());

    }
}
