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
    public void shouldLoadJsonForASimpleRetrySchedule() {
        String retryScheduleName = "Simple-Retry";

        RetryRecord retryRecord = allRetries.get(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.minutes(30)));
    }

    @Test
    public void shouldLoadJsonWithMultiplePeriods() {
        String retryScheduleName = "retry-every-2hrs-and-30mins";
        RetryRecord retryRecord = allRetries.get(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.minutes(30).plusHours(2)));
    }

    @Test
    public void shouldLoadJsonWithMultipleRetries() {
        String retryScheduleName = "retry-every-10Days";
        RetryRecord retryRecord = allRetries.get(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.days(10)));
    }

}
