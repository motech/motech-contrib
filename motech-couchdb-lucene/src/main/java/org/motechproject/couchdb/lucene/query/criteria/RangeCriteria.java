package org.motechproject.couchdb.lucene.query.criteria;

import lombok.EqualsAndHashCode;
import org.motechproject.couchdb.lucene.query.field.RangeField;

@EqualsAndHashCode
public class RangeCriteria implements Criteria {

    private RangeField field;
    private String from;
    private String to;

    public RangeCriteria(RangeField field, String from, String to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    @Override
    public String buildCriteriaString() {
        return String.format("%s<%s>:[%s TO %s]", field.getName(), field.getType().getValue(), from, to);
    }
}
