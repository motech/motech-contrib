package org.motechproject.event.aggregation.aggregate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.aggregation.model.event.AggregatedEventRecord;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.model.schedule.domain.CustomAggregationRecord;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventAggregatorTest {

    EventAggregator eventAggregator;

    @Mock
    private AggregatedEventRecordService aggregatedEventRecordService;
    @Mock
    private AggregationRuleRecordService aggregationRuleRecordService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldAddEventToAggregation() {
        eventAggregator = new EventAggregator("aggregation", asList("foo"), aggregatedEventRecordService, aggregationRuleRecordService);

        AggregationRuleRecord rule = new AggregationRuleRecord("aggregation", "", "event", asList("foo"), new CustomAggregationRecord("true"), "aggregate", AggregationState.Running);
        when(aggregationRuleRecordService.findByName("aggregation")).thenReturn(rule);

        Map<String, Object> params = new HashMap<>();
        params.put("foo", "bar");

        when(aggregatedEventRecordService.find("aggregation", params, new HashMap<String, Object>())).thenReturn(null);

        eventAggregator.handle(new MotechEvent("subject", params));

        AggregatedEventRecord aggregatedEvent = new AggregatedEventRecord("aggregation", params, new HashMap<String, Object>());
        verify(aggregatedEventRecordService).create(aggregatedEvent);
    }

    @Test
    public void shouldMarkAggregateEventIfItHasAnyFieldMissing() {
        eventAggregator = new EventAggregator("aggregation", asList("foo", "fuu"), aggregatedEventRecordService, aggregationRuleRecordService);

        AggregationRuleRecord rule = new AggregationRuleRecord("aggregation", "", "event", asList("foo"), new CustomAggregationRecord("true"), "aggregate", AggregationState.Running);
        when(aggregationRuleRecordService.findByName("aggregation")).thenReturn(rule);

        Map<String, Object> params = new HashMap<>();
        params.put("foo", "bar");

        when(aggregatedEventRecordService.find("aggregation", params, new HashMap<String, Object>())).thenReturn(null);

        eventAggregator.handle(new MotechEvent("subject", params));

        AggregatedEventRecord aggregatedEvent = new AggregatedEventRecord("aggregation", params, new HashMap<String, Object>(), true);
        verify(aggregatedEventRecordService).create(aggregatedEvent);
    }

    @Test
    public void shouldSortKeysInAggregatedParametersMap() {
        eventAggregator = new EventAggregator("aggregation", asList("fee", "foo", "fuu", "fin", "fus"), aggregatedEventRecordService, aggregationRuleRecordService);

        AggregationRuleRecord rule = new AggregationRuleRecord("aggregation", "", "event", asList("foo"), new CustomAggregationRecord("true"), "aggregate", AggregationState.Running);
        when(aggregationRuleRecordService.findByName("aggregation")).thenReturn(rule);

        Map<String, Object> params = new HashMap<>(2);
        params.put("foo", "bar");
        params.put("fus", "bar");
        params.put("fuu", "bur");
        params.put("fee", "baz");
        params.put("fin", "baz");

        when(aggregatedEventRecordService.find("aggregation", params, new HashMap<String, Object>())).thenReturn(null);

        eventAggregator.handle(new MotechEvent("subject", params));

        ArgumentCaptor<AggregatedEventRecord> captor = ArgumentCaptor.forClass(AggregatedEventRecord.class);
        verify(aggregatedEventRecordService).create(captor.capture());
        assertEquals(asList("fee", "fin", "foo", "fus", "fuu"), new ArrayList<>(captor.getValue().getAggregationParams().keySet()));
    }

    @Test
    public void shouldNotAggregateWhilePaused() {
        eventAggregator = new EventAggregator("aggregation", asList("fee", "foo", "fuu", "fin", "fus"), aggregatedEventRecordService, aggregationRuleRecordService);

        AggregationRuleRecord rule = new AggregationRuleRecord("aggregation", "", "event", asList("foo"), new CustomAggregationRecord("true"), "aggregate", AggregationState.Paused);
        when(aggregationRuleRecordService.findByName("aggregation")).thenReturn(rule);

        Map<String, Object> params = new HashMap<>();
        params.put("foo", "bar");
        eventAggregator.handle(new MotechEvent("subject", params));

        verify(aggregatedEventRecordService, never()).create(any(AggregatedEventRecord.class));
    }
}
