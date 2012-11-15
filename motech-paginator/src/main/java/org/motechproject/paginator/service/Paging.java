package org.motechproject.paginator.service;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;

public interface Paging<E> {

    PageResults<E> page(Integer pageNo, Integer rowsPerPage, FilterParams filterCriteria, SortParams sortCriteria);

    String entityName();

}
