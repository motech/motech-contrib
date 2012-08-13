package org.motechproject.export.model;

import org.motechproject.export.annotation.CSVDataSource;
import org.motechproject.export.annotation.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CSVExportProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object csvDataSource;

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
        return new ArrayList<Object>();
    }

    public List<String> columnHeaders() {
        return new ExportDataModel(getDataMethod().getGenericReturnType()).columnHeaders();
    }

    public List<String> rowData(Object model) {
        return new ExportDataModel(getDataMethod().getGenericReturnType()).rowData(model);
    }

    public ExportData getCSVData(Object parameters) {
        List<String> headers = columnHeaders();
        List<List<String>> allRowData = new ArrayList();
        List<Object> data = data(parameters);
        if (data != null && !data.isEmpty()) {
            for (Object datum : data) {
                allRowData.add(rowData(datum));
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
