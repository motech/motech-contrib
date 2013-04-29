package org.motechproject.crud.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CrudEntityTest {

    @Mock
    CrudRepository crudRepository;

    CrudEntity<SomeEntity> crudEntity;

    @Before
    public void setUp() {
        initMocks(this);
        crudEntity = new ExampleCrudEntity(crudRepository);
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
    public void shouldReturnSimpleNameAsEntityName() {
        assertEquals("SomeEntity", crudEntity.entityName());
    }

    @Test
    public void shouldReturnDisplayNameBySeperatingCamelCaseWords() {
        assertEquals("Some Entity", crudEntity.getDisplayName());
    }
}

class SomeEntity extends MotechBaseDataObject {

}

class ExampleCrudEntity extends CouchDBCrudEntity {

    private CrudRepository crudRepository;

    ExampleCrudEntity(CrudRepository crudRepository) {
        super(crudRepository);
    }

    @Override
    public List<String> getDisplayFields() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getFilterFields() {
        return new ArrayList<>();
    }

    @Override
    public Class getEntityType() {
        return SomeEntity.class;
    }
}