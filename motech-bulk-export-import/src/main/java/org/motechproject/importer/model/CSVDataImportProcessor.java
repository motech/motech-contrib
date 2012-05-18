package org.motechproject.importer.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.motechproject.importer.annotation.CSVImporter;

import java.io.FileReader;
import java.util.List;

public class CSVDataImportProcessor extends DataImportProcessor {

    private ColumnPositionMappingStrategy columnPositionMappingStrategy;
    private CsvToBean csvToBean;
    private Object importer;

    public CSVDataImportProcessor(Object importer) {
        super(importer);
        this.importer = importer;
        this.columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
        this.csvToBean = new CsvToBean();
    }

    public String entity() {
        return importer.getClass().getAnnotation(CSVImporter.class).entity();
    }

    public Class bean() {
        return importer.getClass().getAnnotation(CSVImporter.class).bean();
    }

    public List<Object> parse(String filePath) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(filePath), ',');
        String[] headers = reader.readNext();
        columnPositionMappingStrategy.setType(bean());
        columnPositionMappingStrategy.setColumnMapping(headers);
        return csvToBean.parse(columnPositionMappingStrategy, reader);
    }

    public static boolean isValid(Class beanClass) {
        return beanClass.isAnnotationPresent(CSVImporter.class);
    }
}
