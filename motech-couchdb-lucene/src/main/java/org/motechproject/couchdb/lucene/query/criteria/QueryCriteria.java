package org.motechproject.couchdb.lucene.query.criteria;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import java.util.Collection;

@Getter
@EqualsAndHashCode
public class QueryCriteria implements Criteria {
    static final String QUERY_SEPARATOR = ":";
    public static final String OR = " OR ";
    private QueryField field;
    private Object value;

    public QueryCriteria(QueryField field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String buildCriteriaString() {
        if (value instanceof Collection) {
            return buildOrCriteria((Collection) value);
        } else {
            return field.getName() + QUERY_SEPARATOR + field.transform(value.toString());
        }
    }

    private String buildOrCriteria(Collection value) {
        if (value.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (Object element : value) {
                builder.append(field.getName());
                builder.append(":");
                builder.append(field.transform(element.toString()));
                builder.append(OR);
            }
            return builder.substring(0, builder.lastIndexOf(OR)) + ")";
        }
    }
}




