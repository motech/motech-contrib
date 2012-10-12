package org.motechproject.couchdb.lucene.query.field;

import lombok.Getter;
import org.motechproject.couchdb.lucene.query.criteria.Criteria;
import org.motechproject.couchdb.lucene.query.criteria.RangeCriteria;

import java.util.Properties;

@Getter
public class RangeField implements Field {
    private final String name;
    private final FieldType type;
    private String fromName;
    private String toName;

    public RangeField(String name, FieldType type, String fromName, String toName) {
        this.name = name;
        this.type = type;
        this.fromName = fromName;
        this.toName = toName;
    }

    @Override
    public boolean presentIn(Properties filterParams) {
        return filterParams.containsKey(getFromName()) && filterParams.containsKey(getToName());
    }

    @Override
    public Criteria createCriteria(Properties  filterParams) {
        String from = filterParams.get(getFromName()).toString();
        String to = filterParams.get(getToName()).toString();
        return new RangeCriteria(this, from, to);
    }
}
