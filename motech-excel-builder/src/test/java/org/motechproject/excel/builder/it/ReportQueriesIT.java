package org.motechproject.excel.builder.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.excel.builder.config.ReportQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/test-applicationExcelBuilderContext.xml")

public class ReportQueriesIT {

    @Autowired
    private ReportQueries reportQueries;

    @Test
    public void shouldReturnValueForTheGivenKey(){
        String query = reportQueries.getQuery("testReport");

        assertThat(query, is("select id as uniq_id, name as patient_name, start_date_time as started_at from excel_builder.test_report order by uniq_id"));
    }
}
