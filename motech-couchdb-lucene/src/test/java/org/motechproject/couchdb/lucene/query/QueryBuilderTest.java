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
import static org.junit.Assert.assertNull;
import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class QueryBuilderTest {

    @Test
    public void shouldBuildQuery() {
        Properties filterParam = new Properties();
        filterParam.put("field1", "val1");
        filterParam.put("field2From", "val2");
        filterParam.put("field2To", "val3");
        filterParam.put("field3From", "16/10/2012");
        filterParam.put("field3To", "18/12/2012");
        filterParam.put("field4", "17/10/2012");

        QueryBuilder queryBuilder = new QueryBuilder(filterParam, null, new QueryDefinitionImpl());
        String expectedQuery = "field1:val1 AND field2<string>:[val2 TO val3] " +
                "AND field3<date>:[2012-10-16 TO 2012-12-18] " +
                "AND field4:2012-10-17";

        assertThat(queryBuilder.build(), is(expectedQuery));
        assertNull(queryBuilder.buildSortCriteria());
    }

    @Test
    public void shouldBuildSortParams() {
        Properties sortParams = new Properties();
        sortParams.put("field1", "asc");
        sortParams.put("field2", "desc");
        sortParams.put("field3", "asc");

        QueryBuilder queryBuilder = new QueryBuilder(null, sortParams, new QueryDefinitionImpl());
        String expectedCriteria = "/field1<string>,\\field2<string>,/field3<date>";

        assertThat(queryBuilder.buildSortCriteria(), is(expectedCriteria));
    }
}

class QueryDefinitionImpl implements QueryDefinition {
    @Override
    public List<Field> fields() {
        List<Field> fields = new ArrayList<>();
        fields.add(new QueryField("field1", STRING));
        fields.add(new RangeField("field2", STRING, "field2From", "field2To"));
        fields.add(new RangeField("field3", DATE, "field3From", "field3To"));
        fields.add(new QueryField("field4", DATE));
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
                "index.add(doc.field3, {field: 'field3'}); " +
                "index.add(doc.field4, {field: 'field4'}); " +
                "return index;" +
                "}";
    }
};