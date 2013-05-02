package org.motechproject.crud.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.TypeDiscriminator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.MotechBaseDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationCrudModuleContext.xml")
public class CouchDBCrudRepositoryIT  {

    @Autowired
    CouchDbConnector couchDbConnector;
    private Example one;
    private Example two;

    @Test
    public void shouldPerformCrudOperationsOnCouchDB() {
        CouchDBCrudRepository crudRepository = new CouchDBCrudRepository(Example.class, couchDbConnector);
        one = new Example("one");
        two = new Example("two");

        crudRepository.add(one);
        crudRepository.add(two);

        assertEquals(asList(one, two), crudRepository.getAll(0, 3));
        assertEquals(2, crudRepository.count());
        assertEquals(asList(one), crudRepository.findBy("field", "one"));

        two.setField("2");
        crudRepository.save(two); //should update
        assertEquals(asList(two), crudRepository.findBy("field", "2"));
        assertEquals(2, crudRepository.count());
    }

    @After
    public void tearDown() {
        couchDbConnector.delete(one);
        couchDbConnector.delete(two);
    }
}

@Data
@NoArgsConstructor
@TypeDiscriminator("doc.type == 'Example'")
class Example extends MotechBaseDataObject {
    private String field;

    public Example(String field){
        this.field = field;
    }


}
