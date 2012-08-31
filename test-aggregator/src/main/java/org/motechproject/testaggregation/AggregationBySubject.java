package org.motechproject.testaggregation;

import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.motechproject.event.MotechEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AggregationBySubject implements AggregationHandler<MotechEvent> {
    @Override
    public String groupId(MotechEvent event) {
        return event.getSubject();
    }

    @Override
    public boolean canBeDispatched(List<MotechEvent> events) {
        return events.size() == 2;
    }
}