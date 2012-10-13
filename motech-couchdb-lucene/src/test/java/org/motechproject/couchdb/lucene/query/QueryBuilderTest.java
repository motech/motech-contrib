package org.motechproject.couchdb.lucene.query;

import org.junit.Test;
import org.motechproject.couchdb.lucene.query.field.Field;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class QueryBuilderTest {

    @Test
    public void shouldBuildQuery() {
        Properties filterParam = new Properties();
        filterParam.put("field1", "val1");
        filterParam.put("field2From", "val2");
        filterParam.put("field2To", "val3");

        QueryBuilder queryBuilder = new QueryBuilder(filterParam, new QueryDefinitionImpl());
        String expectedQuery = "field1:val1 AND field2<string>:[val2 TO val3]";
        assertThat(queryBuilder.build(), is(expectedQuery));
    }
}

class QueryDefinitionImpl implements QueryDefinition {
    @Override
    public List<Field> fields() {
        List<Field> fields = new ArrayList<>();
        fields.add(new QueryField("field1", STRING));
        fields.add(new RangeField("field2", STRING, "field2From", "field2To"));
        return fields;
    }

    @Override
    public String viewName() {
        return "view_name";
    }

    @Override
    public String searchFunctionName() {
        return "search_name";
    }

    @Override
    public String indexFunction() {
        return "function(doc) { " +
                "var index=new Document(); " +
                "index.add(doc.field1, {field: 'field1'}); " +
                "index.add(doc.field2, {field: 'field2'}); " +
                "return index;" +
                "}";
    }
};