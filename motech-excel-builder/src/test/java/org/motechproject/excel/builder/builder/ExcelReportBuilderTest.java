package org.motechproject.excel.builder.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.excel.builder.service.ExcelExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExcelReportBuilderTest{

    @Mock
    ExcelExporter excelExporter;
    @Mock
    OutputStream outputStream;

    ExcelReportBuilder excelReportBuilder;

    @Before
    public void setUp() {
        initMocks(this);
        excelReportBuilder = new ExcelReportBuilder(excelExporter);
    }

    @Test
    public void shouldCreateWorkBookAndWriteToOutputStream() throws IOException {
        String templateFileName = "template";
        Map params = new HashMap();

        excelReportBuilder.build(outputStream, params, templateFileName);

        verify(excelExporter).export(templateFileName, params);
        verify(outputStream).flush();
    }
}
