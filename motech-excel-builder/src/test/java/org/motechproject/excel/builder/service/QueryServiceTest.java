package org.motechproject.excel.builder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.excel.builder.dao.QueryDAO;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class QueryServiceTest {

    @Mock
    QueryDAO queryDAO;

    QueryService queryService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        queryService = new QueryService(queryDAO);
    }

    @Test
    public void shouldReturnResultSetForGivenReportType(){
        String reportType = "reportType";

        List<Map<String, Object>> resultSet = mock(List.class);
        when(queryDAO.getData(reportType)).thenReturn(resultSet);

        List<Map<String, Object>> expectedResultSet = queryService.getData(reportType);

        assertEquals(expectedResultSet, resultSet);
        verify(queryDAO).getData(reportType);
    }
}
