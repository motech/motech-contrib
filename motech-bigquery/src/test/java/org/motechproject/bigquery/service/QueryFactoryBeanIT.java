package org.motechproject.bigquery.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationReportingBigQueryContext.xml")
public class QueryFactoryBeanIT {

    @Autowired
    AllQueries allQueries;

    @Test
    public void shouldAutowireQueries() {
        assertNotNull(allQueries);
        assertThat(allQueries.getQuery("test.query"), is("select name from patient"));
    }
}
