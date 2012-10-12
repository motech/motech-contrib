package org.motechproject.couchdb.lucene.query.field;

import org.motechproject.couchdb.lucene.query.criteria.Criteria;

import java.util.Properties;

public interface Field {
    boolean presentIn(Properties filterParams);

    Criteria createCriteria(Properties filterParams);
}
