package org.motechproject.retry.dao;

import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-retry.xml")
public class AllRetriesTest {
    @Autowired
    private AllRetries allRetries;

    @Test
    public void shouldLoadJsonFromClasspath() {
        String retryScheduleName = "Simple-Retry";

        RetryRecord retryRecord = allRetries.get(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.minutes(30)));
    }

}
