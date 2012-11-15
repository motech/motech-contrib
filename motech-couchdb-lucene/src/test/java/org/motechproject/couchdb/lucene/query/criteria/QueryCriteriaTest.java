package org.motechproject.couchdb.lucene.query.criteria;

import org.junit.Test;
import org.motechproject.couchdb.lucene.query.field.FieldType;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.couchdb.lucene.query.criteria.QueryCriteria.QUERY_SEPARATOR;

public class QueryCriteriaTest {

    @Test
    public void shouldBuildQueryCriteria(){
        QueryField field = new QueryField("field1", FieldType.STRING);
        String expectedValue = "value1";
        Criteria criteria = new QueryCriteria(field, expectedValue);

        assertThat(criteria.buildCriteriaString(), is(field.getName() + QUERY_SEPARATOR + expectedValue));
    }
}
