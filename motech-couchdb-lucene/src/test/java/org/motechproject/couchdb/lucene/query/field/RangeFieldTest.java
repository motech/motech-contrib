package org.motechproject.couchdb.lucene.query.field;

import org.junit.Test;
import org.motechproject.couchdb.lucene.query.criteria.RangeCriteria;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class RangeFieldTest {

    @Test
    public void shouldCheckIfFieldExistsInFilterParams() {
        RangeField field = new RangeField("field1", STRING, "field1From", "field1To");

        Map<String, Object> filterParamsContainingField = new HashMap<>();
        filterParamsContainingField.put("field1From", "value1");
        filterParamsContainingField.put("field1To", "value2");
        filterParamsContainingField.put("field2", "value3");

        Map<String, Object> filterParamsNotContainingField = new HashMap<>();
        filterParamsNotContainingField.put("field2", "value1");
        filterParamsNotContainingField.put("field3", "value2");

        assertThat(field.presentIn(filterParamsContainingField), is(true));
        assertThat(field.presentIn(filterParamsNotContainingField), is(false));
    }

    @Test
    public void shouldCreateQueryCriteria() {
        RangeField field = new RangeField("field1", STRING, "field1From", "field1To");

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("field1From", "value1");
        filterParams.put("field1To", "value2");
        filterParams.put("field2", "value3");

        assertThat((RangeCriteria) field.createCriteria(filterParams), is(new RangeCriteria(field, "value1", "value2")));
    }

}
