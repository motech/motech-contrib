package org.motechproject.bigquery.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.response.QueryResult;
import org.motechproject.bigquery.service.BigQueryService;

import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BigQueryControllerTest {

    @Mock
    BigQueryService bigQueryService;

    BigQueryController bigQueryController;

    @Before
    public void setUp() {
        initMocks(this);
        bigQueryController = new BigQueryController(bigQueryService);
    }

    @Test
    public void shouldExecuteQuery() throws Exception {
        QueryResult queryResult = new QueryResult(new ArrayList<Map<String, Object>>());
        String expectedJson = new ObjectMapper().writer().writeValueAsString(queryResult);
        String query = "query";

        FilterParams filterParams = new FilterParams();
        filterParams.put("key1", "value1");
        filterParams.put("key2", "value2");
        filterParams.put("emptyValue", "");

        String filterParamJson = new ObjectMapper().writer().writeValueAsString(filterParams);

        FilterParams filterParamsWithoutEmptyValues = filterParams.removeEmptyParams();
        when(bigQueryService.executeQuery(query, filterParamsWithoutEmptyValues)).thenReturn(queryResult);

        standaloneSetup(bigQueryController).build()
                .perform(get("/bigquery/execute").param("queryName", query).param("filterParams", filterParamJson))
                .andExpect(content().string(expectedJson));
    }

    @Test
    public void shouldExecuteQueryWhenThereIsNoFilterParams() throws Exception {
        QueryResult queryResult = new QueryResult(new ArrayList<Map<String, Object>>());
        String expectedJson = new ObjectMapper().writer().writeValueAsString(queryResult);
        String query = "query";

        when(bigQueryService.executeQuery(query, new FilterParams())).thenReturn(queryResult);

        standaloneSetup(bigQueryController).build()
                .perform(get("/bigquery/execute").param("queryName", query))
                .andExpect(content().string(expectedJson));
    }
}
