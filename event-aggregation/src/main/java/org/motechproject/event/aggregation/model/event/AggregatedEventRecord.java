package org.motechproject.event.aggregation.model.event;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;

@Entity
public class AggregatedEventRecord implements AggregatedEvent {

    @Field
    private String aggregationRuleName;

    @Field
    private Map<String, Object> aggregationParams;

    @Field
    private Map<String, Object> nonAggregationParams;

    @Field
    private DateTime timeStamp;

    @Field
    private boolean hasError;

    private AggregatedEventRecord() {
    }

    public AggregatedEventRecord(String aggregationRuleName, Map<String, Object> aggregationParams, Map<String, Object> nonAggregationParams, boolean hasError) {
        this();
        this.aggregationRuleName = aggregationRuleName;
        this.aggregationParams = aggregationParams;
        this.nonAggregationParams = nonAggregationParams;
        this.hasError = hasError;
        this.timeStamp = now();
    }

    public AggregatedEventRecord(String aggregationRuleName, Map<String, Object> aggregationParams, Map<String, Object> nonAggregationParams) {
        this(aggregationRuleName, aggregationParams, nonAggregationParams, false);
    }

    public String getAggregationRuleName() {
        return aggregationRuleName;
    }

    @Override
    public Map<String, Object> getAggregationParams() {
        return aggregationParams;
    }

    @Override
    public Map<String, Object> getNonAggregationParams() {
        return nonAggregationParams;
    }

    @Override
    public DateTime getTimeStamp() {
        return DateUtil.setTimeZoneUTC(timeStamp);
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregatedEventRecord)) {
            return false;
        }

        AggregatedEventRecord that = (AggregatedEventRecord) o;

        if (!aggregationRuleName.equals(that.aggregationRuleName)) {
            return false;
        }
        if (!aggregationParams.equals(that.aggregationParams)) {
            return false;
        }
        if (!nonAggregationParams.equals(that.nonAggregationParams)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = aggregationRuleName.hashCode();
        result = 31 * result + aggregationParams.hashCode();
        result = 31 * result + nonAggregationParams.hashCode();
        return result;
    }
}
