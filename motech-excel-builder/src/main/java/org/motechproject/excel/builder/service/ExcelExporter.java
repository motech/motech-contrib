package org.motechproject.excel.builder.service;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Map;

@Component
public class ExcelExporter {

    public Workbook export(String templateFileName, Map params) {
        XLSTransformer transformer = new XLSTransformer();
        Workbook workbook;
        try {
            InputStream is = new BufferedInputStream(getClass().getResourceAsStream(templateFileName));
            workbook = transformer.transformXLS(is, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }
}
