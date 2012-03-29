package org.motechproject.aggregator.aggregation;

import java.io.Serializable;
import java.util.List;

public interface AggregationHandler<T extends Serializable> {
    String groupId(T event);

    boolean canBeDispatched(List<T> events);
}
