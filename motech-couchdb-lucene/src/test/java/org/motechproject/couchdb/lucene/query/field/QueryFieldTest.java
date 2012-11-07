package org.motechproject.couchdb.lucene.query.field;

import org.junit.Test;
import org.motechproject.couchdb.lucene.query.SortOrder;
import org.motechproject.couchdb.lucene.query.criteria.QueryCriteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QueryFieldTest {

    @Test
    public void shouldCheckIfFieldExistsInFilterParams(){
        QueryField field = new QueryField("field1", FieldType.STRING);

        Map<String, Object> filterParamsContainingField = new HashMap<>();
        filterParamsContainingField.put("field1", "value1");
        filterParamsContainingField.put("field2", "value2");

        Map<String, Object> filterParamsNotContainingField = new HashMap<>();
        filterParamsNotContainingField.put("field2", "value1");
        filterParamsNotContainingField.put("field3", "value2");

        assertThat(field.presentIn(filterParamsContainingField), is(true));
        assertThat(field.presentIn(filterParamsNotContainingField), is(false));
    }

    @Test
    public void shouldCreateQueryCriteria(){
        QueryField field = new QueryField("field1", FieldType.STRING);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("field1", "value1");
        filterParams.put("field2", "value2");

        assertThat((QueryCriteria) field.createCriteria(filterParams), is(new QueryCriteria(field, "value1")));

    }

    @Test
    public void shouldCreateSortCriteria(){
        QueryField field = new QueryField("field1", FieldType.STRING);
        assertThat(field.createSortCriteria(SortOrder.ASC), is("/field1<string>"));
        assertThat(field.createSortCriteria(SortOrder.DESC), is("\\field1<string>"));
    }
}
