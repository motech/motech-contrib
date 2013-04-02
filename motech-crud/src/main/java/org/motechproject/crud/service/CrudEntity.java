package org.motechproject.crud.service;

import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;

import java.util.List;
import java.util.Map;

public abstract class CrudEntity<T> implements Paging {

    private CrudRepository<T> crudRepository;

    protected CrudEntity(CrudRepository<T> crudRepository) {
        this.crudRepository = crudRepository;
    }

    /**
     * Fields you want to display in the listing section
     * @return
     */
    public abstract List<String> getDisplayFields();

    /**
     * Fields you want to show as filters.
     * These fields should have respective "by_fieldName" views defined.
     * @return
     */
    public abstract List<String> getFilterFields();

    /**
     * Fields that should be hidden in the edit form
     * @return
     */
    public abstract List<String> getHiddenFields();

    /**
     * Field name of the ID
     * @return
     */
    public abstract String getIdFieldName();

    /**
     * Default values for field names
     * @return
     */
    public abstract Map<String, String> getDefaultValues();

    /**
     * Type of the entity
     * @return
     */
    public abstract Class getEntityType();

    public String entityName(){
        return this.getEntityType().getSimpleName();
    }

    public CrudRepository getRepository(){
        return crudRepository;
    }

    public PageResults page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria) {
        List<T> result;
        int count;
        filterParams = filterParams.removeEmptyParams();

        if (filterParams.isEmpty()) {
            count = getRepository().count();
            result = getRepository().getAll((pageNumber - 1) * rowsPerPage, rowsPerPage);
        } else {
            Map.Entry<String, Object> entry = filterParams.entrySet().iterator().next();
            result = getRepository().findBy(entry.getKey(), String.valueOf(entry.getValue()));
            count = result.size();
            result = result.subList((pageNumber - 1) * rowsPerPage, Math.min(count, (pageNumber * rowsPerPage)));
        }

        return createPageResults(pageNumber, result, count);
    }

    private PageResults createPageResults(Integer pageNumber, List<T> result, int count) {
        PageResults results = new PageResults();
        results.setPageNo(pageNumber);
        results.setResults(result);
        results.setTotalRows(count);
        return results;
    }
}
