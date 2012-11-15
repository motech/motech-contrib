package org.motechproject.couchdb.lucene.query.criteria;

import lombok.EqualsAndHashCode;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import java.util.Collection;

@EqualsAndHashCode
public class RangeCriteria implements Criteria {

    private RangeField field;
    private Object from;
    private Object to;

    public RangeCriteria(RangeField field, Object from, Object to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    @Override
    public String buildCriteriaString() {
        if (!(from instanceof Collection) && !(to instanceof Collection)) {
            return String.format("%s<%s>:[%s TO %s]",
                    field.getName(),
                    field.getType().getValue(),
                    field.transform(from.toString()),
                    field.transform(to.toString()));
        } else {
            throw new RuntimeException("Range queries must not have multiple criteria");
        }
    }

}
