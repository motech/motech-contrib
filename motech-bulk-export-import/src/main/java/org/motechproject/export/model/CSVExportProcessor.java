package org.motechproject.export.model;

import org.motechproject.export.annotation.CSVDataSource;
import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.motechproject.export.utils.ReflectionUtil.genericReturnType;

public class CSVExportProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object csvDataSource;

    public CSVExportProcessor(Object csvDataSource) {
        this.csvDataSource = csvDataSource;
    }

    public String name() {
        return ReflectionUtil.findAnnotation(csvDataSource.getClass(), CSVDataSource.class).name();
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return ReflectionUtil.hasAnnotation(beanClass, CSVDataSource.class);
    }

    public List<Object> data() {
        try {
            Method method = getDataMethod();
            if (method != null) {
                return (List<Object>) method.invoke(csvDataSource);
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
        return new ExportDataModel(genericReturnType(csvDataSource, getDataMethod().getName())).columnHeaders();
    }

    public List<Object> rowData(Object model) {
        return new ExportDataModel(genericReturnType(csvDataSource, getDataMethod().getName())).rowData(model);
    }

    public ExportData getCSVData() {
        List<String> headers = columnHeaders();
        List<List<Object>> allRowData = new ArrayList();
        List<Object> data = data();
        if (data != null && !data.isEmpty()) {
            for (Object datum : data) {
                allRowData.add(rowData(datum));
            }
        }
        return new ExportData(headers, allRowData);
    }

    private Method getDataMethod() {
        for (Method method : csvDataSource.getClass().getDeclaredMethods()) {
            if (ReflectionUtil.hasAnnotation(method, DataProvider.class)) {
                return method;
            }
        }
        return null;
    }
}
