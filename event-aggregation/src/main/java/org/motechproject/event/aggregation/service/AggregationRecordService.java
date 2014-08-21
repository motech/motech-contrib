package org.motechproject.event.aggregation.service;


import org.motechproject.event.aggregation.model.Aggregation;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;

import java.util.List;

/**
 * This service is a wrapper for {@link org.motechproject.event.aggregation.service.AggregationRuleRecordService}
 * and {@link org.motechproject.event.aggregation.service.AggregatedEventRecordService} that expose methods
 * not available in those services.
 */
public interface AggregationRecordService {

    void addOrReplaceAggregationRule(AggregationRuleRecord aggregationRule);

    List<Aggregation> findAllAggregations(String aggregationRuleName);

    List<Aggregation> findAllErrorEventsForAggregations(String aggregationRuleName);

    void removeByAggregation(Aggregation aggregation);
}
