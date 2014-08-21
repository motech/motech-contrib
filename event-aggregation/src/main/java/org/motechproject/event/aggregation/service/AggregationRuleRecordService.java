package org.motechproject.event.aggregation.service;

import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface AggregationRuleRecordService extends MotechDataService<AggregationRuleRecord> {

    @Lookup(name = "By name")
    AggregationRuleRecord findByName(@LookupField(name = "name") String name);
}
