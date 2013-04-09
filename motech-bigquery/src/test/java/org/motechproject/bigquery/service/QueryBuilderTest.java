package org.motechproject.bigquery.service;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.query.QueryBuilder;
import org.motechproject.bigquery.query.VelocityTemplateBuilder;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {

    QueryBuilder queryBuilder;
    private String sqlTemplate;

    @Before
    public void setUp() {
        queryBuilder = new QueryBuilder(new VelocityTemplateBuilder());
        sqlTemplate = "select patient_id, name from  patient  " +
                " #chain('AND' 'WHERE')" +
                " #chunk($patientId) patient_id = :patientId #end" +
                " #chunk($patientName) name = :patientName #end" +
                " #end" ;
    }

    @Test
    public void shouldBuildQueryFromTemplateAndFilterParams() throws Exception {
        FilterParams filterParams = new FilterParams();
        filterParams.put("patientId", "id");
        filterParams.put("patientName", "name");
        String sql = queryBuilder.build(sqlTemplate, filterParams);

        String expectedSql = "select patient_id, name from  patient   " +
                "WHERE  patient_id = :patientId  AND name = :patientName  ";

        assertEquals(expectedSql, sql);
    }

    @Test
    public void shouldSkipEmptyParamsAndBuildQueryFromTemplateAndFilterParams() throws Exception {
        FilterParams filterParams = new FilterParams();
        filterParams.put("patientId", "id");
        String sql = queryBuilder.build(sqlTemplate, filterParams);

        String expectedSql = "select patient_id, name from  patient   " +
                "WHERE  patient_id = :patientId   ";

        assertEquals(expectedSql, sql);
    }

    @Test
    public void shouldNotAppendWhereClauseForEmptyFilterParams() throws Exception {
        FilterParams filterParams = new FilterParams();
        String expectedSql = "select patient_id, name from  patient   ";

        String sql = queryBuilder.build(sqlTemplate, filterParams);

        assertEquals(expectedSql, sql);
    }
    
    @Test
    public void shouldBuildFromVelocityTemplates() throws Exception {
       String velocitySqlTemplate = "select patient_id, name from  patient #if($patientId) where patient_id = :patientId #end";
        FilterParams filterParams = new FilterParams();
        filterParams.put("patientId", "id");
        String expectedSql = "select patient_id, name from  patient  where patient_id = :patientId ";

        String sql = queryBuilder.build(velocitySqlTemplate, filterParams);

        assertEquals(expectedSql, sql);

        String sqlWithoutWhereClause = "select patient_id, name from  patient ";
        assertEquals(sqlWithoutWhereClause, queryBuilder.build(velocitySqlTemplate, new FilterParams()));
    }

}
