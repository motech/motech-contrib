package org.motechproject.event.aggregation.aggregate;

import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.aggregation.model.event.AggregatedEventRecord;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.motechproject.event.listener.EventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EventAggregator implements EventListener {

    private AggregatedEventRecordService aggregatedEventRecordService;
    private AggregationRuleRecordService aggregationRuleRecordService;

    private String aggregationRuleName;
    private List<String> fields;

    private Logger logger = Logger.getLogger(EventAggregator.class);

    public EventAggregator(String aggregationRuleName, List<String> fields, AggregatedEventRecordService aggregatedEventRecordService,
                           AggregationRuleRecordService aggregationRuleRecordService) {
        this.aggregationRuleName = aggregationRuleName;
        this.fields = fields;
        this.aggregatedEventRecordService = aggregatedEventRecordService;
        this.aggregationRuleRecordService = aggregationRuleRecordService;
    }

    @Override
    public String getIdentifier() {
        return aggregationRuleName;
    }

    @Override
    public void handle(MotechEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("aggregating: " + event);
        }
        AggregationRuleRecord rule = aggregationRuleRecordService.findByName(aggregationRuleName);
        if (rule.getState().equals(AggregationState.Paused)) {
            if (logger.isDebugEnabled()) {
                logger.debug("aggregation[" + aggregationRuleName + "] is paused, ignoring events.");
            }
            return;
        }

        Map<String, Object> aggregationParameters = new TreeMap<>();
        Map<String, Object> nonAggregationParameters = new HashMap<>();
        for (String key : event.getParameters().keySet()) {
            if (fields.contains(key)) {
                aggregationParameters.put(key, event.getParameters().get(key));
            } else {
                nonAggregationParameters.put(key, event.getParameters().get(key));
            }
        }
        AggregatedEventRecord aggregatedEvent = new AggregatedEventRecord(aggregationRuleName, aggregationParameters,
                nonAggregationParameters, hasFieldMissing(event));

        aggregatedEventRecordService.create(aggregatedEvent);
    }

    private boolean hasFieldMissing(MotechEvent event) {
        for (String field : fields) {
            if (!event.getParameters().containsKey(field)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventAggregator that = (EventAggregator) o;

        if (!aggregationRuleName.equals(that.aggregationRuleName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return aggregationRuleName.hashCode();
    }
}
