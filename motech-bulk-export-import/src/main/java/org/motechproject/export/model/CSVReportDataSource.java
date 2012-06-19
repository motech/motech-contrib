package org.motechproject.export.model;

import org.motechproject.export.annotation.CSVReportGroup;
import org.motechproject.export.annotation.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CSVReportDataSource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object controller;

    public CSVReportDataSource(Object controller) {
        this.controller = controller;
    }

    public String name() {
        return controller.getClass().getAnnotation(CSVReportGroup.class).name();
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(CSVReportGroup.class);
    }

    public List<Object> data() {
        try {
            Method method = getDataMethod();
            if (method != null) {
                return (List<Object>) method.invoke(controller);
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
        return new ReportDataModel(getDataMethod().getGenericReturnType()).columnHeaders();
    }

    public List<String> rowData(Object model) {
        return new ReportDataModel(getDataMethod().getGenericReturnType()).rowData(model);
    }

    public ReportData createEntireReport() {
        List<String> headers = columnHeaders();
        List<List<String>> allRowData = new ArrayList();
        List<Object> data = data();
        if (data != null && !data.isEmpty()) {
            for (Object datum : data) {
                allRowData.add(rowData(datum));
            }
        }
        return new ReportData(headers, allRowData);
    }

    private Method getDataMethod() {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Report.class)) {
                return method;
            }
        }
        return null;
    }
}
