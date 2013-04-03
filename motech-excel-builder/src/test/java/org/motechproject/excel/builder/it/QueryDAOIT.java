package org.motechproject.excel.builder.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.excel.builder.dao.QueryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/test-applicationExcelBuilderContext.xml")

public class QueryDAOIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QueryDAO queryDAO;

    @Before
    public void setUp() {
        jdbcTemplate.update("INSERT INTO excel_builder.test_report(id, name, start_date_time) VALUES (123, 'name1', '12-22-2013')");
        jdbcTemplate.update("INSERT INTO excel_builder.test_report(id, name, start_date_time) VALUES (456, 'name2', '11-21-2012')");
        jdbcTemplate.update("INSERT INTO excel_builder.test_report(id, name, start_date_time) VALUES (789, 'name3', '10-20-2011')");
    }

    @Test
    public void shouldReturnDataSet(){
        List<Map<String, Object>> resultSet = queryDAO.getData("testReport");

        assertThat(resultSet.size(), is(3));
        assertThat((String) resultSet.get(0).get("uniq_id"), is("123"));
        assertThat((String) resultSet.get(0).get("patient_name"), is("name1"));
        assertThat((Timestamp) resultSet.get(0).get("started_at"), is(Timestamp.valueOf("2013-12-22 00:00:00.000000")));
    }

    @After
    public void tearDown() {
        jdbcTemplate.update("delete from excel_builder.test_report");
    }
}
