package org.motechproject.excel.builder.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.excel.builder.config.ReportQueries;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class QueryDAOTest {
    @Mock
    private ReportQueries reportQueries;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private QueryDAO queryDAO;

    @Before
    public void setUp() {
        initMocks(this);
        queryDAO = new QueryDAO(jdbcTemplate, reportQueries);
    }

    @Test
    public void shouldExecuteQueryGivenTheReportType() {
        String reportType = "reportType";
        String query = "sql query";
        when(reportQueries.getQuery(reportType)).thenReturn(query);

        List<Map<String, Object>> resultSet = new ArrayList<>();
        when(jdbcTemplate.queryForList(query)).thenReturn(resultSet);

        List<Map<String, Object>> data = queryDAO.getData(reportType);

        assertThat(data, is(resultSet));
    }
}
