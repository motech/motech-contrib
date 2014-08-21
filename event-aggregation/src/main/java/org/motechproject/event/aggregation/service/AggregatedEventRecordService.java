package org.motechproject.event.aggregation.service;

import org.motechproject.event.aggregation.model.event.AggregatedEventRecord;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;
import java.util.Map;

public interface AggregatedEventRecordService extends MotechDataService<AggregatedEventRecord> {

    @Lookup(name = "By aggregation rule name")
    List<AggregatedEventRecord> findByAggregationRulename(@LookupField(name = "aggregationRuleName") String aggregationRuleName);

    @Lookup(name = "By aggregation rule name and error")
    List<AggregatedEventRecord> findByAggregationRulenameAndError(@LookupField(name = "aggregationRuleName") String aggregationRuleName,
                                                                  @LookupField(name = "hasError") Boolean hasError);

    @Lookup(name = "By rule name, aggregation params and non aggregation params")
    List<AggregatedEventRecord> find(@LookupField(name = "aggregationRuleName") String aggregationRuleName,
                                     @LookupField(name = "aggregationParams") Map<String, Object> aggregationParams,
                                     @LookupField(name = "nonAggregationParams") Map<String, Object> nonAggregationParams);
}
