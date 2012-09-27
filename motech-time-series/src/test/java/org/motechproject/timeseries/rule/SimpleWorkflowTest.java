package org.motechproject.timeseries.rule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.service.TimeSeriesService;
import org.motechproject.timeseries.rules.entity.TimeSeriesRule;
import org.motechproject.timeseries.rules.entity.TimeSeriesSubscription;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesRules;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesSubscriptions;
import org.motechproject.timeseries.rules.service.TimeSeriesRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testTimeSeriesContext.xml")
public class SimpleWorkflowTest {

    @Autowired
    AllTimeSeriesRules rules;

    @Autowired
    AllTimeSeriesSubscriptions subscriptions;

    @Autowired
    TimeSeriesRulesService service;

    @Autowired
    TimeSeriesService timeSeriesService;

    @Before
    public void setup() {
        persistRule();
    }

    @Test
    public void shouldExecuteRulesForEntity() {
        TimeSeriesSubscription subscription = subscription();
        service.registerSubscription(subscription);

        TimeSeriesRecord record = buildRecord(subscription);
        timeSeriesService.capture(record);
    }

    private TimeSeriesSubscription subscription() {
        TimeSeriesSubscription subscription = new TimeSeriesSubscription();
        subscription.setExternalId("externalId");
        subscription.setEvent("onRecordEvent");
        subscription.setRuleName("cumulativeMissedDoses");
        return subscription;
    }

    private TimeSeriesRule persistRule() {
        TimeSeriesRule rule = buildRule();
        rules.addContent(rule);
        return rule;
    }

    private TimeSeriesRule buildRule() {
        TimeSeriesRule rule = new TimeSeriesRule();
        rule.setName("cumulativeMissedDoses");
        rule.setInputStream(this.getClass().getClassLoader().getResourceAsStream("cumulativeMissedDoses.drl"));
        return rule;
    }

    private TimeSeriesRecord buildRecord(TimeSeriesSubscription subscription) {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.setExternalId(subscription.getExternalId());
        return record;
    }

    @After
    public void tearDown() {
        rules.removeAll();
        subscriptions.removeAll();
    }
}
