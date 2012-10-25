package org.motechproject.importer.model;

import org.motechproject.importer.annotation.ExcelImporter;

import java.io.Reader;
import java.util.List;

public class ExcelDataImportProcessor extends DataImportProcessor {

    private Object importer;

    public ExcelDataImportProcessor(Object importer) {
        super(importer, importer.getClass().getAnnotation(ExcelImporter.class).bean());
        this.importer = importer;

    }

    public String entity() {
        return importer.getClass().getAnnotation(ExcelImporter.class).entity();
    }

    public List<Object> parse(Reader reader){
        //Implement logic specific to excel parsing
        return null;
    }

    public static boolean isValid(Class beanClass) {
        return beanClass.isAnnotationPresent(ExcelImporter.class);
    }
}
