package org.motechproject.couchdb.lucene.query.field;

import lombok.Getter;
import org.motechproject.couchdb.lucene.query.SortOrder;
import org.motechproject.couchdb.lucene.query.criteria.Criteria;

import java.util.Properties;

@Getter
public abstract class Field {

    protected String name;
    protected FieldType type;
    public abstract boolean presentIn(Properties filterParams);
    public abstract Criteria createCriteria(Properties filterParams);
    abstract String transform(String fieldValue);

    public String createSortCriteria(SortOrder sortOrder){
        return String.format("%s%s<%s>", sortOrder.getLuceneSortPrefix(), name, type.getValue());
    }
}
