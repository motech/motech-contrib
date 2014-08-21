package org.motechproject.event.aggregation.dispatch;

import org.apache.log4j.Logger;
import org.motechproject.event.aggregation.model.Aggregation;
import org.motechproject.event.aggregation.model.AggregationEvent;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.service.AggregationRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
public class EventDispatcher {

    private AggregationRuleRecordService aggregationRuleRecordService;
    private AggregationRecordService aggregationRecordService;
    private final EventRelay eventRelay;

    private Logger log = Logger.getLogger(EventDispatcher.class);

    @Autowired
    public EventDispatcher(AggregationRuleRecordService aggregationRuleRecordService, EventRelay eventRelay,
                           AggregationRecordService aggregationRecordService) {
        this.aggregationRuleRecordService = aggregationRuleRecordService;
        this.eventRelay = eventRelay;
        this.aggregationRecordService = aggregationRecordService;
    }

    public void dispatch(String aggregationRuleName) {
        AggregationRuleRecord aggregationRule = aggregationRuleRecordService.findByName(aggregationRuleName);
        List<Aggregation> aggregations = aggregationRecordService.findAllAggregations(aggregationRule.getName());
        if (log.isInfoEnabled()) {
            log.info(format("publishing aggregation for rule: %s", aggregationRuleName));
        }
        for (Aggregation aggregation : aggregations) {
            eventRelay.sendEventMessage(new AggregationEvent(aggregationRule, aggregation).toMotechEvent());
            aggregationRecordService.removeByAggregation(aggregation);
        }
    }
}
