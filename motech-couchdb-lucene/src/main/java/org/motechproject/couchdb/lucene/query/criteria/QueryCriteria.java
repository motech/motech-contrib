package org.motechproject.couchdb.lucene.query.criteria;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import java.util.Collection;

@Getter
@EqualsAndHashCode
public class QueryCriteria implements Criteria {
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
            return queryString(field.getName(),
                    field.getType().getValue(),
                    field.transform(this.value.toString()));
        }
    }

    private String buildOrCriteria(Collection value) {
        if (value.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (Object element : value) {
                builder.append(queryString(field.getName(),
                        field.getType().getValue(),
                        field.transform(element.toString())));

                builder.append(OR);
            }
            return builder.substring(0, builder.lastIndexOf(OR)) + ")";
        }
    }

    private String queryString(String name, String type, String value) {
        return String.format("%s<%s>:%s",
                name,
                type,
                value);
    }
}




