package org.motechproject.couchdb.lucene.query;

import org.motechproject.couchdb.lucene.query.field.Field;

import java.util.List;

public interface QueryDefinition {
    List<Field> fields();
    String viewName();
    String searchFunctionName();
}
