package org.motechproject.crud.service;

import org.motechproject.crud.model.CrudModel;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;

public interface CrudEntity<T> extends Paging {

    /**
     * A repository to perform CRUD on
     * @return
     */
    CrudRepository getRepository();

    /**
     * A model to render the UI
     * @return
     */
    CrudModel getModel();

    /**
     * Type of the entity
     * @return
     */
    Class getEntityType();

    /**
     * Paging
     * @param pageNumber
     * @param rowsPerPage
     * @param filterParams
     * @param sortCriteria
     * @return
     */
    PageResults page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria);

    /**
     * Post delete handler
     * @param object
     */
    void deleted(T object);

    /**
     * Post update handler
     * @param object
     */
    void updated(T object);
}
