package org.motechproject.event.aggregation.service.impl;

import org.apache.log4j.Logger;
import org.motechproject.event.aggregation.aggregate.EventAggregator;
import org.motechproject.event.aggregation.model.event.PeriodicDispatchEvent;
import org.motechproject.event.aggregation.model.event.SporadicDispatchEvent;
import org.motechproject.event.aggregation.model.mapper.AggregationRuleMapper;
import org.motechproject.event.aggregation.model.rule.AggregationRule;
import org.motechproject.event.aggregation.model.rule.AggregationRuleRequest;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.schedule.AggregationScheduleRequest;
import org.motechproject.event.aggregation.model.schedule.CronBasedAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.CustomAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.PeriodicAggregationRequest;
import org.motechproject.event.aggregation.service.AggregatedEventRecordService;
import org.motechproject.event.aggregation.service.AggregationRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.motechproject.event.aggregation.service.EventAggregationService;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.commons.date.util.DateUtil.now;

@Service
public class EventAggregationServiceImpl implements EventAggregationService {

    private AggregationRecordService aggregationRecordService;
    private AggregationRuleRecordService aggregationRuleRecordService;
    private EventListenerRegistryService eventListenerRegistryService;
    private AggregatedEventRecordService aggregatedEventRecordService;
    private MotechSchedulerService schedulerService;

    public static final int MILLIS_IN_A_SEC = 1000;
    private static final long MILLIS_IN_A_MINUTE = 60 * 1000;
    private AggregationRuleMapper aggregationRuleMapper;
    private Logger logger = Logger.getLogger(EventAggregationService.class);

    @Autowired
    public EventAggregationServiceImpl(AggregationRuleRecordService aggregationRuleRecordService, EventListenerRegistryService eventListenerRegistryService,
                                       AggregatedEventRecordService aggregatedEventRecordService, MotechSchedulerService schedulerService,
                                       AggregationRecordService aggregationRecordService) {
        this.aggregationRuleRecordService = aggregationRuleRecordService;
        this.eventListenerRegistryService = eventListenerRegistryService;
        this.aggregatedEventRecordService = aggregatedEventRecordService;
        this.aggregationRecordService = aggregationRecordService;
        this.schedulerService = schedulerService;
        this.aggregationRuleMapper = new AggregationRuleMapper();
        registerListenersForRules();
    }

    private void registerListenersForRules() {
        for (AggregationRuleRecord rule : aggregationRuleRecordService.retrieveAll()) {
            registerListenerForRule(rule);
        }
    }

    @Override
    public void createRule(AggregationRuleRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("Creating aggregation rule " + request.getName());
        }
        AggregationRuleRecord aggregationRule = aggregationRuleMapper.toRecord(request);

        aggregationRecordService.addOrReplaceAggregationRule(aggregationRule);

        registerListenerForRule(request);

        AggregationScheduleRequest aggregationSchedule = request.getAggregationSchedule();
        if (aggregationSchedule instanceof PeriodicAggregationRequest) {
            PeriodicAggregationRequest periodicSchedule = (PeriodicAggregationRequest) aggregationSchedule;
            schedulerService.safeScheduleRepeatingJob(new RepeatingSchedulableJob(new PeriodicDispatchEvent(aggregationRule.getName()).toMotechEvent(), periodicSchedule.getStartTime().toDate(), null, (long) (periodicSchedule.getPeriod().toStandardSeconds().getSeconds() * MILLIS_IN_A_SEC), true));
        } else if (aggregationSchedule instanceof CronBasedAggregationRequest) {
            CronBasedAggregationRequest cronSchedule = (CronBasedAggregationRequest) aggregationSchedule;
            schedulerService.safeScheduleJob(new CronSchedulableJob(new PeriodicDispatchEvent(aggregationRule.getName()).toMotechEvent(), cronSchedule.getCronExpression()));
        } else if (aggregationSchedule instanceof CustomAggregationRequest) {
            CustomAggregationRequest customSchedule = (CustomAggregationRequest) aggregationSchedule;
            schedulerService.safeScheduleRepeatingJob(new RepeatingSchedulableJob(new SporadicDispatchEvent(aggregationRule.getName(), customSchedule.getRule()).toMotechEvent(), now().toDate(), null, MILLIS_IN_A_MINUTE, true));
        }
    }

    private void registerListenerForRule(AggregationRule rule) {
        if (!eventListenerRegistryService.hasListener(rule.getSubscribedTo())) {
            eventListenerRegistryService.registerListener(new EventAggregator(rule.getName(), rule.getFields(), aggregatedEventRecordService, aggregationRuleRecordService), rule.getSubscribedTo());
        }
    }
}
