package org.motechproject.crud.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JpaCrudRepositoryTest {

    @Mock
    ExampleEntityRepository exampleEntityRepository;

    JpaCrudRepository<ExampleEntity, String> jpaCrudRepository;
    @Before
    public void setUp() {
        initMocks(this);
        jpaCrudRepository = new JpaCrudRepository<>(exampleEntityRepository, String.class);
    }

    @Test
    public void shouldInvokeFindByMethod() {
        String fieldName = "name";
        String fieldValue = "abc";
        List<ExampleEntity> expectedList = asList(new ExampleEntity());
        when(exampleEntityRepository.findByName(fieldValue)).thenReturn(expectedList);

        List<ExampleEntity> actualList = jpaCrudRepository.findBy(fieldName, fieldValue);

        assertEquals(expectedList, actualList);
    }
}

interface ExampleEntityRepository extends JpaRepository<ExampleEntity, String> {
    public List<ExampleEntity> findByName(String name);
}

class ExampleEntity{

}