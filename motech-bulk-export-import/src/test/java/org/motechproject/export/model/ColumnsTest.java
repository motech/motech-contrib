package org.motechproject.export.model;

import org.junit.Test;
import org.motechproject.export.annotation.ExportValue;

import static junit.framework.Assert.assertEquals;

public class ColumnsTest {

    @Test
    public void shouldGetInheritedAnnotatedMethods(){
        Columns columns = new Columns(Dummy.class);
        assertEquals(3,columns.size());
        assertEquals("dummyField",columns.get(0).name());
        assertEquals("district",columns.get(1).name());
        assertEquals("panchy",columns.get(2).name());
    }
    @Test
    public void shouldReturnEmptyIfThereAreNoAnnotatedMethods(){
        Columns columns = new Columns(Object.class);
        assertEquals(0,columns.size());
    }
}


class Dummy extends DummyCsv{
    private String field;

    private String anotherField;

    @ExportValue(column="dummyField", index = 0)
    public String getField() {
        return field;
    }

    private String getAnotherField() {
        return anotherField;
    }
}

