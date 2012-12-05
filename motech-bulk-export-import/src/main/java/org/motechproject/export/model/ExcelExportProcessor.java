package org.motechproject.export.model;

import org.motechproject.export.annotation.*;
import org.motechproject.export.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.*;
import static org.motechproject.export.utils.ReflectionUtil.genericReturnType;

public class ExcelExportProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object excelDataSource;

    public ExcelExportProcessor(Object excelDataSource) {
        this.excelDataSource = excelDataSource;
    }

    public String name() {
        return ReflectionUtil.findAnnotation(excelDataSource.getClass(), ExcelDataSource.class).name();
    }

    public String title() {
        return join(splitByCharacterTypeCamelCase(capitalize(name())), " ");
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return ReflectionUtil.hasAnnotation(beanClass, ExcelDataSource.class);
    }

    public List<String> columnHeaders(String reportName) {
        return new ExportDataModel(genericReturnType(excelDataSource, reportName)).columnHeaders();
    }

    public List<String> rowData(String reportName, Object model) {
        return new ExportDataModel(genericReturnType(excelDataSource, reportName)).rowData(model);
    }

    public ExportData getEntireExcelData(String reportName, Map<String, String> criteria) {
        List<String> headers = columnHeaders(reportName);
        List<List<String>> allRowData = new ArrayList<List<String>>();
        List<Object> data = data(reportName, criteria);
        if (data != null && !data.isEmpty()) {
            for (Object datum : data) {
                allRowData.add(rowData(reportName, datum));
            }
        }

        return new ExportData(headers, allRowData);
    }

    public ExportData getPaginatedExcelData(String reportName) {
        boolean doneBuilding = false;
        int pageNumber = 1;
        List<String> headers = columnHeaders(reportName);
        List<List<String>> allRowData = new ArrayList<List<String>>();

        while (!doneBuilding) {
            List<Object> data = dataForPage(reportName, pageNumber);
            if (data != null && !data.isEmpty()) {
                List<List<String>> rowsOfAPage = new ArrayList<List<String>>();
                for (Object datum : data) {
                    rowsOfAPage.add(rowData(reportName, datum));
                }
                allRowData.addAll(rowsOfAPage);
                pageNumber++;
            } else {
                doneBuilding = true;
            }
        }

        return new ExportData(headers, allRowData);
    }

    private Method getDataMethod(String reportName) {
        for (Method method : excelDataSource.getClass().getDeclaredMethods()) {
            if (ReflectionUtil.hasAnnotation(method, DataProvider.class) && method.getName().equalsIgnoreCase(reportName)) {
                return method;
            }
        }
        return null;
    }

    private Method getHeaderMethod() {
        for (Method method : excelDataSource.getClass().getDeclaredMethods()) {
            if (ReflectionUtil.hasAnnotation(method, Header.class)) {
                return method;
            }
        }
        return null;
    }

    private Method getFooterMethod() {
        for (Method method : excelDataSource.getClass().getDeclaredMethods()) {
            if (ReflectionUtil.hasAnnotation(method, Footer.class)) {
                return method;
            }
        }
        return null;
    }

    public List<Object> dataForPage(String reportName, int pageNumber) {
        return new MethodInvocationBroker().invokeSafely(getDataMethod(reportName), pageNumber);
    }

    public List<Object> data(String reportName, Map<String, String> criteria) {
        return new MethodInvocationBroker().invokeSafely(getDataMethod(reportName), criteria);
    }

    public List<String> customHeaders() {
        return new MethodInvocationBroker().invokeSafely(getHeaderMethod(), null);
    }

    public List<String> customFooters() {
        return new MethodInvocationBroker().invokeSafely(getFooterMethod(), null);
    }

    private class MethodInvocationBroker {

        List invokeSafely(Method method, Object parameters) {
            if (method != null) {
                try {
                    if (parameters != null) {
                        return (List) method.invoke(excelDataSource, parameters);
                    } else {
                        return (List) method.invoke(excelDataSource);
                    }
                } catch (IllegalAccessException e) {
                    String message = "Data method should be public" + e.getMessage();
                    logger.error(message);
                    throw new RuntimeException(message);
                } catch (InvocationTargetException e) {
                    String message = "Format of data method is invalid." + e.getMessage();
                    logger.error(message);
                    throw new RuntimeException(message);
                } catch (IllegalArgumentException e) {
                    String message = "Format of data method is invalid." + e.getMessage();
                    logger.error(message);
                    throw new RuntimeException(message);
                } catch (ClassCastException e) {
                    String message = "Format of data method is invalid." + e.getMessage();
                    logger.error(message);
                    throw new RuntimeException(message);
                }
            } else {
                return Collections.emptyList();
            }
        }
    }
}
