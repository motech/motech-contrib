package org.motechproject.crud.service;

import lombok.Getter;
import org.motechproject.crud.event.CrudEventHandler;
import org.motechproject.crud.model.CrudModel;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;

import java.util.List;
import java.util.Map;

public class CrudEntityImpl<T> implements CrudEntity<T> {

    @Getter
    private CrudRepository<T> repository;
    @Getter
    private CrudModel model;
    @Getter
    private Class entityType;
    private CrudEventHandler eventHandler;

    public CrudEntityImpl(CrudRepository<T> repository, CrudModel model, Class entityType, CrudEventHandler eventHandler) {
        this.repository = repository;
        this.model = model;
        this.entityType = entityType;
        this.eventHandler = eventHandler;
    }

    @Override
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

    @Override
    public String entityName() {
        return entityType.getSimpleName();
    }

    private PageResults createPageResults(Integer pageNumber, List<T> result, int count) {
        PageResults results = new PageResults();
        results.setPageNo(pageNumber);
        results.setResults(result);
        results.setTotalRows(count);
        return results;
    }

    @Override
    public void deleted(T object) {
        if(eventHandler != null)
            eventHandler.deleted(object);
    }

    @Override
    public void updated(T object) {
        if(eventHandler != null)
            eventHandler.updated(object);
    }
}
