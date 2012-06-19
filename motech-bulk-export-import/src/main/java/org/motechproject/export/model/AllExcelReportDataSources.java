package org.motechproject.export.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Map;

import static org.motechproject.export.model.ExcelReportDataSource.isValidDataSource;

@Repository
public class AllExcelReportDataSources implements BeanPostProcessor {

    private Map<String, ExcelReportDataSource> reportDataSources = new Hashtable<String, ExcelReportDataSource>();

    public ExcelReportDataSource get(String groupName) {
        return reportDataSources.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isValidDataSource(bean.getClass())) {
            ExcelReportDataSource dataSource = new ExcelReportDataSource(bean);
            reportDataSources.put(dataSource.name(), dataSource);
        }
        return bean;
    }
}
