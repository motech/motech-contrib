package org.motechproject.bigquery.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.response.QueryResult;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BigQueryDAOTest {

    BigQueryDAO bigQueryDAO;

    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    SqlQueryParamBuilder sqlQueryParamBuilder;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        bigQueryDAO = new BigQueryDAO(jdbcTemplate, sqlQueryParamBuilder);
    }

    @Test
    public void shouldExecuteQuery() {
        FilterParams filterParams = mock(FilterParams.class);
        Map queryParams = mock(Map.class);

        when(sqlQueryParamBuilder.buildQueryParams(filterParams)).thenReturn(queryParams);
        String query = "query";
        List expectedList = mock(List.class);

        when(jdbcTemplate.queryForList(query, queryParams)).thenReturn(expectedList);
        QueryResult queryResult = bigQueryDAO.executeQuery(query, filterParams);

        assertEquals(new QueryResult(expectedList), queryResult);
    }
}
