package org.motechproject.crud.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.crud.service.CrudEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.motechproject.crud.builder.CrudEntityBuilder.newCrudEntity;
import static org.motechproject.crud.builder.CrudModelBuilder.couchDBCrudModel;

public class CrudEntityBuilderTest {

    @Test
    public void shouldBuildCrudEntity() {
        CrudModelBuilder modelBuilder = couchDBCrudModel(ExampleClass.class);
        CrudRepository crudRepository = mock(CrudRepository.class);
        CrudEntity entity = newCrudEntity(ExampleClass.class)
                .crudRepository(crudRepository)
                .model(modelBuilder)
                .build();

        assertEquals(modelBuilder.build(), entity.getModel());
        assertEquals(crudRepository, entity.getRepository());
        assertEquals(ExampleClass.class, entity.getEntityType());
        assertEquals("Example Class", entity.getModel().getDisplayName());
    }

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenModelIsNotSet() {
        CrudEntityBuilder crudEntityBuilder = new CrudEntityBuilder(ExampleClass.class);
        crudEntityBuilder.crudRepository(mock(CrudRepository.class));

        exceptionThrown.expect(RuntimeException.class);
        exceptionThrown.expectMessage(CrudEntityBuilder.MANDATORY_FIELDS_MESSAGE);

        crudEntityBuilder.build();
    }

    @Test
    public void shouldThrowExceptionWhenRepositoryIsNotSet() {
        CrudEntityBuilder crudEntityBuilder = new CrudEntityBuilder(ExampleClass.class);
        crudEntityBuilder.model(couchDBCrudModel(ExampleClass.class));

        exceptionThrown.expect(RuntimeException.class);
        exceptionThrown.expectMessage(CrudEntityBuilder.MANDATORY_FIELDS_MESSAGE);

        crudEntityBuilder.build();
    }
}

class ExampleClass { }