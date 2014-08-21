package org.motechproject.event.aggregation.dispatch;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.aggregation.model.event.AggregatedEvent;
import org.motechproject.event.aggregation.model.event.EventStrings;
import org.motechproject.event.aggregation.model.event.SporadicDispatchEvent;
import org.motechproject.event.aggregation.rule.RuleAgent;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SporadicDispatcher {

    private EventDispatcher eventDispatcher;
    private AggregatedEventRecordService aggregatedEventRecordService;

    @Autowired
    public SporadicDispatcher(EventDispatcher eventDispatcher, AggregatedEventRecordService aggregatedEventRecordService) {
        this.eventDispatcher = eventDispatcher;
        this.aggregatedEventRecordService = aggregatedEventRecordService;
    }

    @MotechListener(subjects = EventStrings.SPORADIC_DISPATCH_EVENT)
    public void handle(MotechEvent event) {
        SporadicDispatchEvent dispatchEvent = new SporadicDispatchEvent(event);
        String aggregationRuleName = dispatchEvent.getAggregationRuleName();
        List<? extends AggregatedEvent> aggregatedEvents = aggregatedEventRecordService.findByAggregationRulename(aggregationRuleName);

        if (new RuleAgent(dispatchEvent.getExpression(), aggregatedEvents).execute()) {
            eventDispatcher.dispatch(aggregationRuleName);
        }
    }
}
