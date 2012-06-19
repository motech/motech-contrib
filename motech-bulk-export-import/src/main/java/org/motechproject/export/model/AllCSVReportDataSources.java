package org.motechproject.export.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Map;

import static org.motechproject.export.model.CSVReportDataSource.isValidDataSource;

@Repository
public class AllCSVReportDataSources implements BeanPostProcessor {

    private Map<String, CSVReportDataSource> reportDataSources = new Hashtable<String, CSVReportDataSource>();

    public CSVReportDataSource get(String groupName) {
        return reportDataSources.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isValidDataSource(bean.getClass())) {
            CSVReportDataSource dataSource = new CSVReportDataSource(bean);
            reportDataSources.put(dataSource.name(), dataSource);
        }
        return bean;
    }
}
