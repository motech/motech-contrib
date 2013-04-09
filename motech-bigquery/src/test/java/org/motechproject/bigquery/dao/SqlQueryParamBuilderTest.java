package org.motechproject.bigquery.dao;

import org.junit.Test;
import org.motechproject.bigquery.model.FilterParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SqlQueryParamBuilderTest {

    private SqlQueryParamBuilder sqlQueryParamBuilder;

    @Test
    public void shouldConvertFilterParamsIntoCorrespondingDataType() throws ParseException {
        sqlQueryParamBuilder = new SqlQueryParamBuilder();

        FilterParams filterParams = new FilterParams();
        filterParams.put("name_string", "name");
        String date = "21/01/1988";
        filterParams.put("birth_date", date);
        filterParams.put("age_int", "25");

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("name_string", "name");
        expectedParams.put("birth_date", new SimpleDateFormat("dd/MM/yyyy").parse(date));
        expectedParams.put("age_int", 25);

        Map<String,Object> actualParams = sqlQueryParamBuilder.buildQueryParams(filterParams);

        assertThat(actualParams,is(expectedParams));
    }
}
