package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.model.Workbook;

import java.util.List;

public abstract class ExcelBuilder<T> {

    private Workbook workbook;

    protected ExcelBuilder(String title, List<String> columnHeaders,  List<String> customHeaders,  List<String> customFooters) {
        workbook = new Workbook(title, columnHeaders, customHeaders, customFooters);
    }

    public HSSFWorkbook build() {
        List<T> data = data();
        if (data != null) {
            for (T datum : data) {
                workbook.addRow(createRowData(datum));
            }
        }
        return workbook.book();
    }

    protected abstract List<String> createRowData(T modal);

    protected abstract List<T> data();
}
