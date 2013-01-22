package org.motechproject.retry;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.service.RetryService;
import org.motechproject.retry.utils.EventHandler;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class RetryServiceIT {
    @Autowired
    private RetryService retryService;
    @Autowired
    private AllRetries allRetries;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private EventHandler eventHandler;

    private transient boolean eventFired;

    @Test
    public void shouldCreateRetryEvent() {
        String groupName = "campaign-retries";
        String name = "retry-every-2hrs-and-30mins";
        String externalId = "externalId";
        DateTime referenceTime = DateTime.now();

        retryService.schedule(new RetryRequest(name, externalId, referenceTime), new HashMap<String, Object>());

        Retry activeRetry = allRetries.getActiveRetry(externalId, name);
        assertThat(activeRetry.hasPendingRetires(), is(true));
        assertThat(activeRetry.startTime(), is(referenceTime.plus(Period.minutes(30))));

        retryService.fulfill(externalId, groupName);

        assertNull(allRetries.getActiveRetry(externalId, name));
    }

    @Test
    public void shouldInvokeRetrySubjectListener() throws InterruptedException {
        String externalId = UUID.randomUUID().toString();
        retryService.schedule(new RetryRequest("retry-every-second", externalId, DateTime.now()), new HashMap<String, Object>());
        int counter = 0;
        while(true) {
            if(eventHandler.hasEventBeenFired()) {
                retryService.fulfill(externalId, "retry-every-second");
                return;
            }
            if(counter >= 70) {
                fail("Retry listener should have been invoked");
            }
            counter++;
            Thread.sleep(1000);
        }
    }

    @After
    public void tearDown() {
        eventHandler.reset();
        allRetries.removeAll();
        removeQuartzJobs();
    }

    private void removeQuartzJobs() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            List<String> groupNames = scheduler.getJobGroupNames();
            for (String group : groupNames) {
                Set<JobKey> jobNames = scheduler.getJobKeys(jobGroupEquals(group));
                for (JobKey jobKey : jobNames)
                    scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
