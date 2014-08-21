package org.motechproject.event.aggregation.service.impl;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.aggregation.aggregate.EventAggregator;
import org.motechproject.event.aggregation.model.event.PeriodicDispatchEvent;
import org.motechproject.event.aggregation.model.event.SporadicDispatchEvent;
import org.motechproject.event.aggregation.model.mapper.AggregationRuleMapper;
import org.motechproject.event.aggregation.model.rule.AggregationRuleRequest;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.schedule.CronBasedAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.CustomAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.PeriodicAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.domain.CronBasedAggregationRecord;
import org.motechproject.event.aggregation.model.schedule.domain.PeriodicAggregationRecord;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.aggregation.service.AggregationRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.motechproject.event.aggregation.service.EventAggregationService;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.testing.utils.TimeFaker.fakeNow;
import static org.motechproject.testing.utils.TimeFaker.stopFakingTime;

public class EventAggregationServiceImplTest {

    @Mock
    private AggregationRuleRecordService aggregationRuleRecordService;
    @Mock
    EventListenerRegistryService eventListenerRegistryService;
    @Mock
    private AggregatedEventRecordService aggregatedEventRecordService;
    @Mock
    private AggregationRecordService aggregationRecordService;
    @Mock
    private MotechSchedulerService schedulerService;

    EventAggregationService eventAggregationService;
    
    private final long MILLIS_IN_A_DAY = Period.days(1).toStandardSeconds().getSeconds() * 1000;
    private final long MILLIS_IN_A_MINUTE = 60 * 1000;
    private AggregationRuleMapper aggregationRuleMapper;

    @Before
    public void setup() {
        initMocks(this);
        eventAggregationService = new EventAggregationServiceImpl(aggregationRuleRecordService, eventListenerRegistryService,
                aggregatedEventRecordService, schedulerService, aggregationRecordService);
        aggregationRuleMapper = new AggregationRuleMapper();
    }

    @Test
    public void shouldCreateAggregationRule() {
        eventAggregationService.createRule(new AggregationRuleRequest("foo", "", "event", new ArrayList<String>(), new PeriodicAggregationRequest("1 day", newDateTime(2012, 5, 22)), "aggregation", AggregationState.Running));

        verify(aggregationRecordService).addOrReplaceAggregationRule(aggregationRuleMapper.toRecord(new AggregationRuleRequest("foo", "", "event", new ArrayList<String>(), new PeriodicAggregationRequest("1 day", newDateTime(2012, 5, 22)), "aggregation", AggregationState.Running)));
    }

    @Test
    public void shouldSubscribeToAggregatedEventsOnStartup() {
        List<AggregationRuleRecord> rules = asList(
            new AggregationRuleRecord("foo1", "", "event1", asList("bar", "maz"), new PeriodicAggregationRecord(Period.days(1),
                    newDateTime(2012, 5, 22)), "aggregation", AggregationState.Running),
            new AggregationRuleRecord("foo3", "", "event3", asList("bar", "maz"), new CronBasedAggregationRecord("* * * * *"),
                    "aggregation", AggregationState.Running)
        );
        when(aggregationRuleRecordService.retrieveAll()).thenReturn(rules);

        EventListenerRegistryService mockEventListenerRegistryService = mock(EventListenerRegistryService.class);
        eventAggregationService = new EventAggregationServiceImpl(aggregationRuleRecordService, mockEventListenerRegistryService,
                aggregatedEventRecordService, schedulerService, aggregationRecordService);

        verify(mockEventListenerRegistryService).registerListener(new EventAggregator("foo1", asList("bar", "maz"),
                aggregatedEventRecordService, aggregationRuleRecordService), "event1");
        verify(mockEventListenerRegistryService).registerListener(new EventAggregator("foo3", asList("bar", "maz"),
                aggregatedEventRecordService, aggregationRuleRecordService), "event3");
    }

    @Test
    public void shouldSubscribeToAggregationEventsForAllRulesOnStartup() {
        eventAggregationService.createRule(new AggregationRuleRequest("foo", "", "event", asList("bar", "maz"),
                new PeriodicAggregationRequest("1 day", newDateTime(2012, 5, 22)), "aggregation", AggregationState.Running));

        verify(eventListenerRegistryService).registerListener(new EventAggregator("foo", asList("bar", "maz"),
                aggregatedEventRecordService, aggregationRuleRecordService), "event");
    }



    @Test
    public void shouldScheduleRepeatJobToDispatchPeriodicAggregation() {
        AggregationRuleRequest request = new AggregationRuleRequest("foo", "", "event", asList("bar", "maz"), new PeriodicAggregationRequest("1 day", newDateTime(2010, 10, 1)), "aggregation", AggregationState.Running);
        eventAggregationService.createRule(request);

        RepeatingSchedulableJob job = new RepeatingSchedulableJob(new PeriodicDispatchEvent("foo").toMotechEvent(), newDateTime(2010, 10, 1).toDate(), null, MILLIS_IN_A_DAY, true);

        verify(schedulerService).safeScheduleRepeatingJob(job);
    }

    @Test
    public void shouldScheduleCronJobToDispatchCronBasedAggregation() {
        AggregationRuleRequest request = new AggregationRuleRequest("foo", "", "event", asList("bar", "maz"), new CronBasedAggregationRequest("* * * * *"), "aggregation", AggregationState.Running);
        eventAggregationService.createRule(request);

        CronSchedulableJob job = new CronSchedulableJob(new PeriodicDispatchEvent("foo").toMotechEvent(), "* * * * *");

        verify(schedulerService).safeScheduleJob(job);
    }
    
    @Test
    public void shouldScheduleMinutelyJobToDispatchAggregationsInARollingWindow() {
        try {
            fakeNow(newDateTime(2010, 10, 1));

            AggregationRuleRequest request = new AggregationRuleRequest("foo", "", "event", asList("bar", "maz"), new CustomAggregationRequest("exp"), "aggregation", AggregationState.Running);
            eventAggregationService.createRule(request);

            RepeatingSchedulableJob job = new RepeatingSchedulableJob(new SporadicDispatchEvent("foo", "exp").toMotechEvent(), newDateTime(2010, 10, 1).toDate(), null, MILLIS_IN_A_MINUTE, true);

            verify(schedulerService).safeScheduleRepeatingJob(job);
        } finally {
            stopFakingTime();
        }
    }
}
