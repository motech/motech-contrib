package org.motechproject.importer.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.ColumnName;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

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

    private Map<String, String> getColumnMapping() {
        Map<String, String> mapping = new HashMap<String, String>();
        List<Member> members = getAllAnnotatedMembers();
        for (Member member : members) {
            if (member instanceof Field) {
                addFieldDescription(mapping, (Field) member);
            } else if (member instanceof Method) {
                addMethodDescription(mapping, (Method) member);
            }
        }
        return mapping;
    }

    private void addMethodDescription(Map<String, String> mapping, Method member) {
        Method method = member;
        if (method.isAnnotationPresent(ColumnName.class)) {
            mapping.put(method.getAnnotation(ColumnName.class).name(), method.getName().replace("set", ""));
        } else {
            mapping.put(method.getName(), method.getName());
        }
    }

    private void addFieldDescription(Map<String, String> mapping, Field member) {
        Field field = member;
        if (field.isAnnotationPresent(ColumnName.class)) {
            mapping.put(field.getAnnotation(ColumnName.class).name(), field.getName());
        } else {
            mapping.put(field.getName(), field.getName());
        }
    }

    private List<Member> getAllAnnotatedMembers() {
        List<Member> members = new ArrayList<Member>();
        members.addAll(asList(bean().getDeclaredFields()));
        members.addAll(asList(bean().getDeclaredMethods()));
        return members;
    }
}
