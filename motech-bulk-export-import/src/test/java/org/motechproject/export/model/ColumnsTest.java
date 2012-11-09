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
        assertEquals("parentField1",columns.get(1).name());
        assertEquals("parentField2",columns.get(2).name());
    }
    @Test
    public void shouldReturnEmptyIfThereAreNoAnnotatedMethods(){
        Columns columns = new Columns(Object.class);
        assertEquals(0,columns.size());
    }
}


class Dummy extends DummyParent{
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

class DummyParent{

    private String parentField1;
    private String parentField2;

    public void methodFromParent(){}

    @ExportValue(column="parentField1", index = 1)
    public String getParentField1() {
        return parentField1;
    }
    @ExportValue(column="parentField2", index = 2)
    public String getParentField2() {
        return parentField2;
    }
}