package org.motechproject.retry.dao;

import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-retry.xml")
public class AllRetriesDefinitionTest {

    @Autowired
    private AllRetriesDefinition allRetriesDefinition;

    @Test
    public void shouldLoadJsonForASimpleRetrySchedule() {
        String retryScheduleName = "Simple-Retry";

        RetryRecord retryRecord = allRetriesDefinition.getRetryRecord(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.minutes(30)));
    }


    @Test
    public void shouldLoadJsonWithMultiplePeriods() {
        String retryScheduleName = "retry-every-2hrs-and-30mins";
        RetryRecord retryRecord = allRetriesDefinition.getRetryRecord(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.minutes(30).plusHours(2)));
    }

    @Test
    public void shouldLoadJsonWithMultipleRetries() {
        String retryScheduleName = "retry-every-10Days";
        RetryRecord retryRecord = allRetriesDefinition.getRetryRecord(retryScheduleName);

        assertThat(retryRecord.name(), is(retryScheduleName));
        assertThat(retryRecord.retryCount(), is(5));
        assertThat(retryRecord.retryInterval(), is(Period.days(10)));
    }


    @Test
    public void shouldGetNextRetryRecord() {
        RetryRecord nextRetryRecord = allRetriesDefinition.getNextRetryRecord("retry-every-2hrs-and-30mins");

        assertNotNull(nextRetryRecord);
        assertThat(nextRetryRecord.name(), is("retry-every-10Days"));
    }

    @Test
    public void shouldGetAllRetryRecordNames() {
        List<String> recordNames = allRetriesDefinition.getAllRetryRecordNames("campaign-retries");
        assertReflectionEquals(recordNames, asList("retry-every-2hrs-and-30mins", "retry-every-10Days"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void shouldReturnNullIfNoRetryRecordIsPresentNext() {
        assertNull(allRetriesDefinition.getNextRetryRecord("retry-every-10Days"));
    }

    @Test
    public void shouldGetRetryGrouName() {
        assertThat(allRetriesDefinition.getRetryGroupName("retry-every-2hrs-and-30mins"), is("campaign-retries"));
    }

}
