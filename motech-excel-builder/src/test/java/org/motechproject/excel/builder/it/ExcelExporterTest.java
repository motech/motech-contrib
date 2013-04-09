package org.motechproject.excel.builder.it;

import bad.robot.excel.valuetypes.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class ExcelExporterTest extends ExcelTest{

    private Workbook workbook;
    public static final String TEMPLATE_RESULT_KEY = "data";
    public static final String TEMPLATE_FILE_NAME = "/xls/templates/testTemplate.xls";
    public static final Date started_at_date = new Date();
    public static final Timestamp started_at = new Timestamp(started_at_date.getTime());

    @Test
    public void shouldBuildExcelWithDataProvided() throws IOException {
        Map<String, Object> patient1 = new HashMap(){{
            put("uniq_id", 123);
            put("patient_name", "name1");
            put("started_at", started_at);
        }};
        Map<String, Object> patient2 = new HashMap(){{
            put("uniq_id", 456);
            put("patient_name", "name2");
            put("started_at", started_at);
        }};

        List<Map<String, Object>> resultSet = new ArrayList<>();
        resultSet.add(patient1);
        resultSet.add(patient2);


        Map params = new HashMap();
        params.put(TEMPLATE_RESULT_KEY, resultSet);

        workbook = excelExporter.export(TEMPLATE_FILE_NAME, params);

        assertThat(stringValue(ExcelColumnIndex.A, 1), is(equalTo("Patient Records")));
        assertThat(numericValue(ExcelColumnIndex.A, 3), is(equalTo((double) 123)));
        assertThat(stringValue(ExcelColumnIndex.B, 3), is(equalTo("name1")));
        assertThat(dateValue(ExcelColumnIndex.C, 3), is(started_at_date));

        assertThat(numericValue(ExcelColumnIndex.A, 4), is(equalTo((double) 456)));
        assertThat(stringValue(ExcelColumnIndex.B, 4), is(equalTo("name2")));
        assertThat(dateValue(ExcelColumnIndex.C, 4), is(started_at_date));
    }

    @Override
    Workbook getWorkbook() {
        return workbook;
    }
}
