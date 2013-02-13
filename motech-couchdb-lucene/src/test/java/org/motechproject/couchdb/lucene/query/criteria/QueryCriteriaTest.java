package org.motechproject.couchdb.lucene.query.criteria;

import org.junit.Test;
import org.motechproject.couchdb.lucene.query.field.FieldType;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QueryCriteriaTest {

    @Test
    public void shouldBuildQueryCriteria(){
        QueryField field = new QueryField("field1", FieldType.STRING);
        String expectedValue = "value1";
        Criteria criteria = new QueryCriteria(field, expectedValue);

        assertThat(criteria.buildCriteriaString(), is("field1<string>:value1"));
    }

    @Test
    public void shouldBuildQueryCriteriaForCollection(){
        QueryField field = new QueryField("field1", FieldType.STRING);

        Criteria criteria = new QueryCriteria(field, asList("value1", "value2"));

        assertThat(criteria.buildCriteriaString(), is("(field1<string>:value1 OR field1<string>:value2)"));
    }
}
