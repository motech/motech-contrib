package org.motechproject.event.aggregation.model.rule.domain;

import org.motechproject.event.aggregation.model.rule.AggregationRule;
import org.motechproject.event.aggregation.model.rule.AggregationState;
import org.motechproject.event.aggregation.model.schedule.domain.AggregationScheduleRecord;
import org.motechproject.mds.annotations.Entity;

import java.util.List;

@Entity
public class AggregationRuleRecord implements AggregationRule {

    private String name;
    private String description;
    private String subscribedTo;
    private List<String> fields;
    private AggregationScheduleRecord aggregationSchedule;
    private String publishAs;
    private AggregationState state;

    public AggregationRuleRecord() {
        this.state = AggregationState.Running;
    }

    public AggregationRuleRecord(String name, String description, String subscribedTo, List<String> fields, AggregationScheduleRecord schedule, String publishAs, AggregationState state) {
        this();
        this.name = name;
        this.description = description;
        this.subscribedTo = subscribedTo;
        this.fields = fields;
        this.aggregationSchedule = schedule;
        this.publishAs = publishAs;
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSubscribedTo() {
        return subscribedTo;
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public AggregationScheduleRecord getAggregationSchedule() {
        return aggregationSchedule;
    }

    @Override
    public String getPublishAs() {
        return publishAs;
    }

    @Override
    public AggregationState getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubscribedTo(String subscribedTo) {
        this.subscribedTo = subscribedTo;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setAggregationSchedule(AggregationScheduleRecord aggregationSchedule) {
        this.aggregationSchedule = aggregationSchedule;
    }

    public void setPublishAs(String publishAs) {
        this.publishAs = publishAs;
    }

    public void setState(AggregationState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AggregationRuleRecord that = (AggregationRuleRecord) o;

        if (!aggregationSchedule.equals(that.aggregationSchedule)) {
            return false;
        }
        if (!fields.equals(that.fields)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        if (!publishAs.equals(that.publishAs)) {
            return false;
        }
        if (!subscribedTo.equals(that.subscribedTo)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + subscribedTo.hashCode();
        result = 31 * result + fields.hashCode();
        result = 31 * result + aggregationSchedule.hashCode();
        result = 31 * result + publishAs.hashCode();
        return result;
    }
}
