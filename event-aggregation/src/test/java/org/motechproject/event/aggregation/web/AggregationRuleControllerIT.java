package org.motechproject.event.aggregation.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.aggregation.model.event.EventStrings;
import org.motechproject.event.aggregation.model.mapper.AggregationRuleMapper;
import org.motechproject.event.aggregation.model.rule.AggregationRuleRequest;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.model.rule.domain.AggregationRuleRecord;
import org.motechproject.event.aggregation.model.schedule.CronBasedAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.CustomAggregationRequest;
import org.motechproject.event.aggregation.model.schedule.PeriodicAggregationRequest;
import org.motechproject.event.aggregation.service.AggregationRecordService;
import org.motechproject.event.aggregation.service.AggregationRuleRecordService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/motech/*.xml"})
public class AggregationRuleControllerIT {

    private MockMvc mockAggregationRuleController;

    @Autowired
    AggregationRuleController aggregationRuleController;

    @Autowired
    AggregationRuleRecordService aggregationRuleRecordService;

    @Autowired
    AggregationRecordService aggregationRecordService;

    @Autowired
    private MotechSchedulerService schedulerService;

    private AggregationRuleMapper aggregationRuleMapper;

    @Before
    public void setup() throws Exception {
        mockAggregationRuleController = MockMvcBuilders.standaloneSetup(aggregationRuleController).build();
        aggregationRuleMapper = new AggregationRuleMapper();
    }

    @After
    public void teardown() {
        aggregationRuleRecordService.deleteAll();
    }

    @Test
    public void shouldReturnAllRulesAsJson() throws Exception {
        List<AggregationRuleRecord> rules = asList(
            aggregationRuleMapper.toRecord(new AggregationRuleRequest("aggregation1", "", "subscribedEvent1", asList("foo"),
                    new CronBasedAggregationRequest("* * * * * ?"), "publishEvent1", AggregationState.Running)),
            aggregationRuleMapper.toRecord(new AggregationRuleRequest("aggregation2", "", "subscribedEvent2", asList("fuu"),
                    new CustomAggregationRequest(""), "publishEvent2", AggregationState.Running))
        );
        aggregationRecordService.addOrReplaceAggregationRule(rules.get(0));
        aggregationRecordService.addOrReplaceAggregationRule(rules.get(1));

        mockAggregationRuleController.perform(
            get("/rules"))
        .andExpect(
            content().string(new ObjectMapper().writeValueAsString(aggregationRuleRecordService.retrieveAll())));
    }

    @Test
    public void shouldReturnASingleRuleByNameAsJson() throws Exception {
        List<AggregationRuleRequest> rules = asList(
            new AggregationRuleRequest("aggregation1", "eve", "subscribedEvent1", asList("foo"),
                    new CronBasedAggregationRequest("* * * * * ?"), "publishEvent1", AggregationState.Running),
            new AggregationRuleRequest("aggregation2", "eve", "subscribedEvent2", asList("fuu"),
                    new CustomAggregationRequest("true"), "publishEvent2", AggregationState.Running)
        );
        aggregationRecordService.addOrReplaceAggregationRule(aggregationRuleMapper.toRecord(rules.get(0)));
        aggregationRecordService.addOrReplaceAggregationRule(aggregationRuleMapper.toRecord(rules.get(1)));

        mockAggregationRuleController.perform(
            get("/rules/aggregation2"))
        .andExpect(
            content().string(new ObjectMapper().writeValueAsString(rules.get(1))));
    }

    @Test
    public void shouldCreateARule() throws Exception {
        final String externalId = "aggregation1";
        AggregationRuleRequest ruleRequest = new AggregationRuleRequest(externalId, "", "subscribedEvent1", asList("foo"),
                new CronBasedAggregationRequest("* * * * * ?"), "publishEvent1", AggregationState.Running);

        try {
            mockAggregationRuleController.perform(
                    put("/rules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new ObjectMapper().writeValueAsString(ruleRequest).getBytes("UTF-8")))
                    .andExpect(
                            status().is(201)
                    );
            assertNotNull(aggregationRuleRecordService.findByName(externalId));
        } finally {
            schedulerService.safeUnscheduleJob(EventStrings.PERIODIC_DISPATCH_EVENT, externalId);
        }
    }

    @Test
    public void shouldReplaceAnExistingRule() throws Exception {
        final String externalId1 = "aggregation1";
        final String externalId2 = "aggregation1";
        List<AggregationRuleRecord> rules = asList(
                aggregationRuleMapper.toRecord(new AggregationRuleRequest(externalId1, "", "subscribedEvent1", asList("foo"),
                        new CronBasedAggregationRequest("* * * * * ?"), "publishEvent1", AggregationState.Running)),
                aggregationRuleMapper.toRecord(new AggregationRuleRequest(externalId2, "", "subscribedEvent2", asList("fuu"),
                        new CustomAggregationRequest(""), "publishEvent2", AggregationState.Running))
        );

        try {
            aggregationRecordService.addOrReplaceAggregationRule(rules.get(0));
            aggregationRecordService.addOrReplaceAggregationRule(rules.get(1));

            AggregationRuleRequest updatedRule = new AggregationRuleRequest(externalId1, "", "subscribedEvent3", asList("foo"),
                    new CronBasedAggregationRequest("* * * * * ?"), "publishEvent3", AggregationState.Running);

            mockAggregationRuleController.perform(
                    put("/rules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new ObjectMapper().writeValueAsString(updatedRule).getBytes("UTF-8")))
                    .andExpect(
                            status().is(201)
                    );
            assertEquals("subscribedEvent3", aggregationRuleRecordService.findByName(externalId1).getSubscribedTo());
        } finally {
            schedulerService.safeUnscheduleJob(EventStrings.PERIODIC_DISPATCH_EVENT, externalId1);
            schedulerService.safeUnscheduleRepeatingJob(EventStrings.PERIODIC_DISPATCH_EVENT, externalId2);
        }
    }

    @Test
    public void shouldReturnHttp400ForBadJson() throws Exception {
        mockAggregationRuleController.perform(
            put("/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .body("foobar".getBytes("UTF-8")))
            .andExpect(
                status().is(400));
    }

    @Test
    public void shouldValidateRequestFields() throws Exception {
        AggregationRuleRequest request = new AggregationRuleRequest(null, "foo", "bar", asList("baz"),
                new CustomAggregationRequest("true"), "fuu", AggregationState.Running);
        FieldError error = new FieldError("name", "Must be present");
        mockAggregationRuleController.perform(
            put("/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ObjectMapper().writeValueAsString(request).getBytes()))
            .andExpect(
                status().is(400))
            .andExpect(
                content().string(new ObjectMapper().writeValueAsString(asList(error))));
    }

    @Test
    public void shouldValidateNestedFields() throws Exception {
        AggregationRuleRequest request = new AggregationRuleRequest("aggregation", "", "subscribeEvent", asList("foo"),
                new PeriodicAggregationRequest("foo", newDateTime(2012, 10, 5)), "publishEvent", AggregationState.Running);
        FieldError error = new FieldError("aggregationSchedule.period", "Not a valid period value");
        mockAggregationRuleController.perform(
            put("/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ObjectMapper().writeValueAsString(request).getBytes()))
            .andExpect(
                status().is(400))
            .andExpect(
                content().string(new ObjectMapper().writeValueAsString(asList(error))));
    }

    @Test
    public void shouldDeleteAnExistingRule() throws Exception {
        List<AggregationRuleRecord> rules = asList(
            aggregationRuleMapper.toRecord(new AggregationRuleRequest("aggregation1", "", "subscribedEvent1", asList("foo"),
                    new CronBasedAggregationRequest("* * * * * ?"), "publishEvent1", AggregationState.Running)),
            aggregationRuleMapper.toRecord(new AggregationRuleRequest("aggregation2", "", "subscribedEvent2", asList("fuu"),
                    new CustomAggregationRequest(""), "publishEvent2", AggregationState.Running))
        );
        aggregationRecordService.addOrReplaceAggregationRule(rules.get(0));
        aggregationRecordService.addOrReplaceAggregationRule(rules.get(1));

        mockAggregationRuleController.perform(
            delete("/rules/aggregation2"));

        List<AggregationRuleRecord> allRules = aggregationRuleRecordService.retrieveAll();
        assertEquals(asList(rules.get(0)), allRules);
    }
}
