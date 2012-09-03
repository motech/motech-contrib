package org.motechproject.paginator.repository;

import org.motechproject.paginator.service.Paging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AllPagingServices implements BeanPostProcessor {

    private Map<String, Paging> pagingServiceMap;

    public AllPagingServices() {
        this.pagingServiceMap = new HashMap<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Paging) {
            pagingServiceMap.put(((Paging) bean).entityName(), (Paging) bean);
        }
        return bean;
    }

    public Paging getPagingServiceFor(String beanName) {
        return pagingServiceMap.get(beanName);
    }
}
