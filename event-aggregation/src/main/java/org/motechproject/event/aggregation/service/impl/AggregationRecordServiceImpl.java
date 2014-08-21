package org.motechproject.event.aggregation.service.impl;

import org.motechproject.event.aggregation.model.Aggregation;
import org.motechproject.event.aggregation.model.event.AggregatedEventRecord;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.aggregation.service.AggregationRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AggregationRecordServiceImpl implements AggregationRecordService {

    @Autowired
    private AggregatedEventRecordService aggregatedEventRecordService;

    @Autowired
    private AggregationRuleRecordService aggregationRuleRecordService;

    public void addOrReplaceAggregationRule(AggregationRuleRecord aggregationRule) {
        if (aggregationRuleRecordService.findByName(aggregationRule.getName()) == null) {
            aggregationRuleRecordService.create(aggregationRule);
        } else {
            aggregationRuleRecordService.update(aggregationRule);
        }
    }

    @Override
    public List<Aggregation> findAllAggregations(String aggregationRuleName) {
        List<AggregatedEventRecord> aggregatedEventRecords = aggregatedEventRecordService.findByAggregationRulenameAndError(aggregationRuleName, false);
        List<Aggregation> aggregations = new LinkedList<>();
        aggregations.add(new Aggregation(aggregationRuleName, aggregatedEventRecords));
        return aggregations;
    }

    @Override
    public List<Aggregation> findAllErrorEventsForAggregations(String aggregationRuleName) {
        List<AggregatedEventRecord> aggregatedEventRecords = aggregatedEventRecordService.findByAggregationRulenameAndError(aggregationRuleName, true);
        List<Aggregation> aggregations = new LinkedList<>();
        aggregations.add(new Aggregation(aggregationRuleName, aggregatedEventRecords));
        return aggregations;
    }

    @Override
    public void removeByAggregation(Aggregation aggregation) {
        for (AggregatedEventRecord event : aggregation.getEventRecords()) {
            aggregatedEventRecordService.delete(event);
        }
    }
}
