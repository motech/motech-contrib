package org.motechproject.timeseries.rule;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.rules.entity.TimeSeriesRule;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesRules;
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
    TimeSeriesRulesService service;

    @Test
    public void shouldExecuteRulesForEntity() {
        TimeSeriesRule timeSeriesRule = persistRule();
        TimeSeriesRecord record = buildRecord(timeSeriesRule);

        service.executeRules(record, timeSeriesRule.getTrigger());
    }

    private TimeSeriesRule persistRule() {
        TimeSeriesRule rule = buildRule();
        rules.addContent(rule);
        return rule;
    }

    private TimeSeriesRule buildRule() {
        TimeSeriesRule rule = new TimeSeriesRule();
        rule.setExternalId("externalId");
        rule.setInputStream(this.getClass().getClassLoader().getResourceAsStream("cumulativeMissedDoses.drl"));
        rule.setTrigger("onRecordEvent");
        return rule;
    }

    private TimeSeriesRecord buildRecord(TimeSeriesRule timeSeriesRule) {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.setExternalId(timeSeriesRule.getExternalId());
        return record;
    }

    @After
    public void tearDown() {
        rules.removeAll();
    }
}
