package org.motechproject.importer.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.ColumnName;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVDataImportProcessor extends DataImportProcessor {

    private CsvToBean csvToBean;
    private Object importer;

    public CSVDataImportProcessor(Object importer) {
        super(importer);
        this.importer = importer;
        this.csvToBean = new CsvToBean();
    }

    public String entity() {
        return importer.getClass().getAnnotation(CSVImporter.class).entity();
    }

    public Class bean() {
        return importer.getClass().getAnnotation(CSVImporter.class).bean();
    }

    private Map<String, String> getColumnMapping() {
        Map<String, String> mapping = new HashMap<String, String>();
        for (Field field : bean().getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnName.class))
                mapping.put(field.getAnnotation(ColumnName.class).name(), field.getName());
            else
                mapping.put(field.getName(), field.getName());
        }
        return mapping;
    }

    public List<Object> parse(String filePath) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(filePath), ',');
        HeaderColumnNameTranslateMappingStrategy columnNameMappingStrategy = new HeaderColumnNameTranslateMappingStrategy();
        columnNameMappingStrategy.setType(bean());
        columnNameMappingStrategy.setColumnMapping(getColumnMapping());
        return csvToBean.parse(columnNameMappingStrategy, reader);
    }

    public static boolean isValid(Class beanClass) {
        return beanClass.isAnnotationPresent(CSVImporter.class);
    }
}
