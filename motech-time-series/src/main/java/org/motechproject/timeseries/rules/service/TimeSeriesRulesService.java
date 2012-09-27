package org.motechproject.timeseries.rules.service;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.rules.entity.TimeSeriesRule;
import org.motechproject.timeseries.rules.repository.AllTimeSeriesRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSeriesRulesService {

    private AllTimeSeriesRules timeSeriesRules;

    @Autowired
    public TimeSeriesRulesService(AllTimeSeriesRules timeSeriesRules) {
        this.timeSeriesRules = timeSeriesRules;
    }

    public void executeRules(TimeSeriesRecord record, String event) {
        List<TimeSeriesRule> timeSeriesRules = this.timeSeriesRules.withTrigger(record.getExternalId(), event);
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
        return kBuilder;
    }
}
