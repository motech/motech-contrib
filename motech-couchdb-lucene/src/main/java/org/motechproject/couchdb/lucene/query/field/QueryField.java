package org.motechproject.couchdb.lucene.query.field;

import lombok.Getter;
import org.motechproject.couchdb.lucene.query.criteria.Criteria;
import org.motechproject.couchdb.lucene.query.criteria.QueryCriteria;

import java.util.Properties;

@Getter
public class QueryField implements Field {
    private String name;
    private FieldType type;

    public QueryField(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean presentIn(Properties filterParams) {
        return filterParams.containsKey(getName());
    }

    @Override
    public Criteria createCriteria(Properties filterParams) {
        return new QueryCriteria(this, filterParams.get(getName()).toString());
    }
}
