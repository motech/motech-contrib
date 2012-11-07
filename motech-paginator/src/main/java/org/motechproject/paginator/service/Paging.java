package org.motechproject.paginator.service;

import org.motechproject.paginator.response.PageResults;

import java.util.Map;
import java.util.Properties;

public interface Paging<E> {

    PageResults<E> page(Integer pageNo, Integer rowsPerPage, Properties filterCriteria, Properties sortCriteria);

    String entityName();

}
