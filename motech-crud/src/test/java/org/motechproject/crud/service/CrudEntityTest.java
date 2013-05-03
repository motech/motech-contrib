package org.motechproject.crud.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.crud.builder.CrudEntityBuilder;
import org.motechproject.crud.event.CrudEventHandler;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.crud.builder.CrudModelBuilder.couchDBCrudModel;

public class CrudEntityTest {

    @Mock
    CrudRepository crudRepository;
    CrudEntity crudEntity;

    @Before
    public void setUp() {
        initMocks(this);
        crudEntity = new CrudEntityBuilder(SomeEntity.class)
                .crudRepository(crudRepository)
                .model(couchDBCrudModel(SomeEntity.class))
                .build();
    }

    @Test
    public void shouldPageAllRecords_whenFilterParamsAreEmpty() {
        int expectedCount = 2;
        List<SomeEntity> expectedResults = asList(new SomeEntity(), new SomeEntity());
        when(crudRepository.count()).thenReturn(expectedCount);
        when(crudRepository.getAll(0, 10)).thenReturn(expectedResults);

        FilterParams emptyFilterParams = new FilterParams();
        emptyFilterParams.put("name", "");

        PageResults page = crudEntity.page(1, 10, emptyFilterParams, new SortParams());

        assertEquals(expectedCount, page.getTotalRows().intValue());
        assertEquals(expectedResults, page.getResults());
    }

    @Test
    public void shouldFilterRecords_whenFilterParamsAreSet() {
        List<SomeEntity> expectedResults = asList(new SomeEntity(), new SomeEntity());
        String filterName = "name";
        String filterValue = "abcd";

        when(crudRepository.findBy(filterName, filterValue)).thenReturn(expectedResults);

        FilterParams filterParams = new FilterParams();
        filterParams.put(filterName, filterValue);

        PageResults page = crudEntity.page(1, 10, filterParams, new SortParams());

        assertEquals(expectedResults.size(), page.getTotalRows().intValue());
        assertEquals(expectedResults, page.getResults());
    }

    @Test
    public void shouldNotInvokeEventHandlerIfNull() {
        SomeEntity object = mock(SomeEntity.class);
        crudEntity.updated(object);
        crudEntity.deleted(object);
    }

    @Test
    public void shouldInvokeEventHandlerWhenItIsNotNull() {
        SomeEntity object = mock(SomeEntity.class);
        CrudEventHandler eventHandler = mock(CrudEventHandler.class);
        CrudEntity crudEntityWithEventHandler = new CrudEntityBuilder(SomeEntity.class)
                .crudRepository(crudRepository)
                .model(couchDBCrudModel(SomeEntity.class))
                .eventHandler(eventHandler)
                .build();

        crudEntityWithEventHandler.updated(object);
        verify(eventHandler).updated(object);

        crudEntityWithEventHandler.deleted(object);
        verify(eventHandler).deleted(object);

    }
    @Test
    public void shouldReturnSimpleNameAsEntityName() {
        assertEquals("SomeEntity", crudEntity.entityName());
    }

}

class SomeEntity extends MotechBaseDataObject {

}
