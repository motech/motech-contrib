package org.motechproject.bigquery.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.bigquery.dao.BigQueryDAO;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.query.QueryBuilder;
import org.motechproject.bigquery.response.QueryResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BigQueryServiceTest {

    BigQueryService bigQueryService;

    @Mock
    BigQueryDAO bigQueryDAO;
    @Mock
    AllQueries allQueries;
    @Mock
    QueryBuilder queryBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        bigQueryService = new BigQueryService(allQueries, bigQueryDAO, queryBuilder);
    }

    @Test
    public void shouldExecuteQueryForGivenQueryName() {
        String queryName = "queryName";
        String query = "query";
        String evaluatedQuery = "evaluatedQuery";

        FilterParams filterParams = new FilterParams();
        QueryResult expectedQueryResult = mock(QueryResult.class);

        when(allQueries.getQuery(queryName)).thenReturn(query);
        when(queryBuilder.build(query, filterParams)).thenReturn(evaluatedQuery);
        when(bigQueryDAO.executeQuery(evaluatedQuery, filterParams)).thenReturn(expectedQueryResult);

        assertEquals(expectedQueryResult, bigQueryService.executeQuery(queryName, filterParams));

    }
}
