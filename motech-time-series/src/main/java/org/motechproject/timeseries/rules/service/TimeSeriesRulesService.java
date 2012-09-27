package org.motechproject.timeseries.rules.service;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.rules.entity.TimeSeriesRule;
import org.motechproject.timeseries.rules.entity.TimeSeriesSubscription;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesRules;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesSubscriptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSeriesRulesService {

    private AllTimeSeriesRules timeSeriesRules;
    private AllTimeSeriesSubscriptions timeSeriesSubscriptions;

    @Autowired
    public TimeSeriesRulesService(AllTimeSeriesRules timeSeriesRules, AllTimeSeriesSubscriptions timeSeriesSubscriptions) {
        this.timeSeriesRules = timeSeriesRules;
        this.timeSeriesSubscriptions = timeSeriesSubscriptions;
    }

    public void registerSubscription(TimeSeriesSubscription subscription) {
        timeSeriesSubscriptions.add(subscription);
    }

    public void executeRules(TimeSeriesRecord record, String event) {
        List<TimeSeriesRule> timeSeriesRules = this.timeSeriesRules.withNames(timeSeriesSubscriptions.ruleNames(record.getExternalId(), event));
        StatelessKnowledgeSession statelessKnowledgeSession = knowledgeSession(timeSeriesRules);
        statelessKnowledgeSession.execute(record);
    }

    private StatelessKnowledgeSession knowledgeSession(List<TimeSeriesRule> timeSeriesRules) {
        KnowledgeBase knowledgeBase = knowledgeBase(timeSeriesRules);
        return knowledgeBase.newStatelessKnowledgeSession();
    }

    private KnowledgeBase knowledgeBase(List<TimeSeriesRule> timeSeriesRules) {
        KnowledgeBuilder kBuilder = knowledgeBuilder(timeSeriesRules);
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(kBuilder.getKnowledgePackages());
        return knowledgeBase;
    }

    private KnowledgeBuilder knowledgeBuilder(List<TimeSeriesRule> timeSeriesRules) {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (TimeSeriesRule timeSeriesRule : timeSeriesRules) {
            kBuilder.add(ResourceFactory.newInputStreamResource(timeSeriesRule.getInputStream()), ResourceType.DRL);
        }
        if (kBuilder.hasErrors()) {
            for (KnowledgeBuilderError err : kBuilder.getErrors()) {
                System.out.println(err.toString());
            }
        }
        return kBuilder;
    }
}
