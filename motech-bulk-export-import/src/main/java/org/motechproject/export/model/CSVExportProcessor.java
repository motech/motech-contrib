package org.motechproject.export.model;

import org.motechproject.export.annotation.CSVDataSource;
import org.motechproject.export.annotation.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
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
        try {
            Method method = getDataMethod();
            if (method != null) {
                return parameters != null ? (List<Object>) method.invoke(csvDataSource, parameters) : (List<Object>) method.invoke(csvDataSource);
            }
        } catch (IllegalAccessException e) {
            logger.error("Data method should be public" + e.getMessage());
            throw new RuntimeException("Data method should be public" + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        } catch (ClassCastException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
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
        List<String> headers = new ArrayList<>();
        List<List<String>> allRowData = new ArrayList();
        if (data != null && !data.isEmpty()) {
            Class<? extends Object> clazz = data.get(0).getClass();
            headers = columnHeaders(clazz);
            for (Object datum : data) {
                allRowData.add(rowData(datum, clazz));
            }
        }
        return new ExportData(headers, allRowData);
    }

    private Method getDataMethod() {
        for (Method method : csvDataSource.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DataProvider.class)) {
                return method;
            }
        }
        return null;
    }
}
