package org.motechproject.couchdb.lucene.query.field;

import org.motechproject.couchdb.lucene.query.criteria.Criteria;
import org.motechproject.couchdb.lucene.query.criteria.QueryCriteria;

import java.util.List;
import java.util.Properties;


public class QueryField extends Field {

    public QueryField(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean presentIn(Properties filterParams) {
        return filterParams.containsKey(name);
    }

    @Override
    public Criteria createCriteria(Properties filterParams) {
        return new QueryCriteria(this, filterParams.get(name));
    }

    @Override
    public String transform(String fieldValue) {
        return type.transform(fieldValue);
    }


}
