package org.motechproject.retry;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.service.RetryServiceImpl;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-retry.xml")
@Ignore
public class RetryServiceIT {
    @Autowired
    private RetryServiceImpl retryServiceImpl;
    @Autowired
    private AllRetries allRetries;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Test
    public void shouldCreateRetryEvent() {
        String groupName = "campaign-retries";
        String name = "retry-every-2hrs-and-30mins";
        String externalId = "externalId";
        DateTime startTime = DateTime.now();
        DateTime referenceTime = DateTime.now();

        retryServiceImpl.schedule(new RetryRequest(name, externalId, startTime, referenceTime));

        Retry activeRetry = allRetries.getActiveRetry(externalId, name);
        assertThat(activeRetry.hasRetriesLeft(), is(true));
        assertThat(activeRetry.startTime(), is(startTime));

        retryServiceImpl.fulfill(externalId, groupName);

        assertNull(allRetries.getActiveRetry(externalId, name));
    }

    @After
    public void tearDown() {
        allRetries.removeAll();
        removeQuartzJobs();
    }

    private void removeQuartzJobs() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            String[] groupNames = scheduler.getJobGroupNames();
            for (String group : groupNames) {
                String[] jobNames = scheduler.getJobNames(group);
                for (String job : jobNames)
                    scheduler.deleteJob(job, group);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
