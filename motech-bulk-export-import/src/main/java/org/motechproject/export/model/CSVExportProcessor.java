package org.motechproject.export.model;

import org.motechproject.export.annotation.CSVDataSource;
import org.motechproject.export.annotation.ComponentTypeProvider;
import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CSVExportProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object csvDataSource;

    public CSVExportProcessor() {
    }

    public CSVExportProcessor(Object csvDataSource) {
        this.csvDataSource = csvDataSource;
    }

    public String name() {
        return csvDataSource.getClass().getAnnotation(CSVDataSource.class).name();
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(CSVDataSource.class);
    }

    public List<Object> data(Object parameters) {
        Method method = ReflectionUtil.getAnnotatedMethod(csvDataSource, DataProvider.class);
        if (method != null) {
            if (parameters != null)
                return (List<Object>) ReflectionUtil.invokeMethod(method, csvDataSource, parameters);
            return (List<Object>) ReflectionUtil.invokeMethod(method, csvDataSource);
        }
        return new ArrayList<>();
    }

    public List<String> columnHeaders(Type returnType) {
        return new ExportDataModel(returnType).columnHeaders();
    }

    public List<String> rowData(Object model, Type returnType) {
        return new ExportDataModel(returnType).rowData(model);
    }

    public ExportData getCSVData(Object parameters) {
        List data = data(parameters);
        return formatCSVData(data);
    }

    public ExportData formatCSVData(List data) {
        List<String> headers;
        List<List<String>> allRowData = new ArrayList();
        if (data != null && !data.isEmpty()) {
            Class<? extends Object> clazz = data.get(0).getClass();
            headers = columnHeaders(clazz);
            for (Object datum : data) {
                allRowData.add(rowData(datum, clazz));
            }
        } else
            headers = getColumnHeadersEvenThoughDataIsNull(data);
        return new ExportData(headers, allRowData);
    }

    private List<String> getColumnHeadersEvenThoughDataIsNull(List data) {
        Class componentClassType = getComponentClassType(data);
        return columnHeaders(componentClassType);
    }

    private Class getComponentClassType(List data) {
        Object componentClass = ReflectionUtil.invokeAnnotatedMethod(data, ComponentTypeProvider.class);
        return componentClass != null ? (Class) componentClass : Object.class;
    }
}
