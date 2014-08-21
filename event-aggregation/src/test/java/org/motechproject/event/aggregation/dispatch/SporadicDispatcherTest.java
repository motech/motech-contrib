package org.motechproject.event.aggregation.dispatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.aggregation.model.event.SporadicDispatchEvent;
import org.motechproject.event.aggregation.model.mapper.AggregationRuleMapper;
import org.motechproject.event.aggregation.model.rule.AggregationRuleRequest;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.schedule.PeriodicAggregationRequest;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

public class SporadicDispatcherTest {

    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private AggregatedEventRecordService aggregatedEventRecordService;

    private SporadicDispatcher sporadicDispatcher;
    private AggregationRuleMapper aggregationRuleMapper;

    @Before
    public void setup() {
        initMocks(this);
        sporadicDispatcher = new SporadicDispatcher(eventDispatcher, aggregatedEventRecordService);
        aggregationRuleMapper = new AggregationRuleMapper();
    }

    @Test
    public void shouldPublishAggregatedEventWhenRuleIsSatisfied() {
        AggregationRuleRecord aggregationRule = aggregationRuleMapper.toRecord(new AggregationRuleRequest(
            "my_aggregation", "", "event", asList("foo", "fuu"), new PeriodicAggregationRequest("1 day",
                newDateTime(2012, 5, 22)), "aggregated_event", AggregationState.Running));

        sporadicDispatcher.handle(new SporadicDispatchEvent("my_aggregation", "true").toMotechEvent());

        verify(eventDispatcher).dispatch("my_aggregation");
    }

    @Test
    public void shouldPublishAggregatedEventIfRuleIsNotSatisfied() {
        AggregationRuleRecord aggregationRule = aggregationRuleMapper.toRecord(new AggregationRuleRequest(
            "my_aggregation", "", "event", asList("foo", "fuu"), new PeriodicAggregationRequest("1 day",
                newDateTime(2012, 5, 22)), "aggregated_event", AggregationState.Running));

        sporadicDispatcher.handle(new SporadicDispatchEvent("my_aggregation", "false").toMotechEvent());

        verify(eventDispatcher, never()).dispatch("my_aggregation");
    }
}
