package org.motechproject.bigquery.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationReportingBigQueryContext.xml")
public class QueriesIT {

    @Autowired
    AllQueries allQueries;

    @Test
    public void shouldGetQuery() {
        assertNotNull(allQueries.getQuery("test.query"));
    }
}
