package org.motechproject.importer.model;

import org.motechproject.importer.annotation.ExcelImporter;

import java.util.List;

public class ExcelDataImportProcessor extends DataImportProcessor {

    private Object importer;

    public ExcelDataImportProcessor(Object importer) {
        super(importer);
        this.importer = importer;
    }

    public String entity() {
        return importer.getClass().getAnnotation(ExcelImporter.class).entity();
    }

    public Class bean() {
        return importer.getClass().getAnnotation(ExcelImporter.class).bean();
    }

    public List<Object> parse(String filePath) throws Exception {
        //Implement logic specific to excel parsing
        return null;
    }

    public static boolean isValid(Class beanClass) {
        return beanClass.isAnnotationPresent(ExcelImporter.class);
    }
}
