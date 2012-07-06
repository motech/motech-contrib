package org.motechproject.retry.dao;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.motechproject.retry.domain.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class AllRetriesTest {
    @Autowired
    private AllRetries allRetries;

    @Test
    public void shouldAddRetryIdempotently() {
        AllRetries spy = spy(allRetries);

        DateTime startTime = DateTime.now();
        Retry retry = new Retry("retry", "externalId", startTime, 5, Period.days(2));
        spy.createRetry(retry);

        Retry anotherRetry = new Retry("retry", "externalId", startTime, 5, Period.days(2));
        spy.createRetry(anotherRetry);

        Mockito.verify(spy, times(1)).add(Matchers.<Retry>any());
    }

    @Test
    public void shouldGetActiveRetry() {

        String retryName = "retryName";
        String externalId = "externalId";
        allRetries.createRetry(new Retry(retryName, externalId, DateTime.now(), 2, Period.hours(2)));

        assertNotNull(allRetries.getActiveRetry(externalId, retryName));
    }

    @After
    public void tearDown() {
        allRetries.removeAll();
    }

}
