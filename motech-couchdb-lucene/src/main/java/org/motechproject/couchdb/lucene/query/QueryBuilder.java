package org.motechproject.couchdb.lucene.query;

import ch.lambdaj.Lambda;
import org.apache.commons.lang.StringUtils;
import org.motechproject.couchdb.lucene.query.criteria.Criteria;
import org.motechproject.couchdb.lucene.query.field.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.core.Is.is;
import static org.motechproject.couchdb.lucene.query.SortOrder.getSortOrder;

public class QueryBuilder {
    public static final String AND = " AND ";
    public static final String EMPTY_STRING = "";
    public static final String LUCENE_SORT_FIELD_SEPARATOR = ",";
    private final Map<String, Object> filterParams;
    private LinkedHashMap<String, Object> sortParams;
    private final QueryDefinition queryDefinition;

    public QueryBuilder(Map<String, Object> filterParams, LinkedHashMap<String, Object> sortParams, QueryDefinition queryDefinition) {
        this.filterParams = filterParams;
        this.sortParams = sortParams;
        this.queryDefinition = queryDefinition;
    }

    public String build() {
        List<Criteria> criteria = getQueryCriteria();
        return getQuery(criteria);
    }

    private String getQuery(List<Criteria> criteria) {
        StringBuilder builder = new StringBuilder();
        for (Criteria criterion : criteria) {
            builder.append(criterion.buildCriteriaString());
            builder.append(AND);
        }

        if (builder.length() == 0)
            return EMPTY_STRING;

        return builder.substring(0, builder.lastIndexOf(AND));
    }

    private List<Criteria> getQueryCriteria() {
        List<Criteria> criteria = new ArrayList<>();
        for (Field field : queryDefinition.fields()) {
            if (field.presentIn(filterParams)) {
                criteria.add(field.createCriteria(filterParams));
            }
        }
        return criteria;
    }

    public String buildSortCriteria() {
        if (sortParams == null) {
            return null;
        }

        List<String> criteria = new ArrayList<>();

        for(String key : sortParams.keySet()) {
            Field selectedField = Lambda.selectFirst(queryDefinition.fields(), having(on(Field.class).getName(), is(key)));
            if(selectedField != null)
                criteria.add(selectedField.createSortCriteria(getSortOrder((String) sortParams.get(key))));
        }

        return StringUtils.join(criteria, LUCENE_SORT_FIELD_SEPARATOR);
    }
}
