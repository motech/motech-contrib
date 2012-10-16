package org.motechproject.couchdb.lucene.query.field;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldTypeTest {

    @Test
    public void shouldConvertDateToLuceneFormat(){
        assertEquals("2012-10-16", FieldType.DATE.transform("16/10/2012"));
    }

    @Test
    public void shouldNotTransformFieldValue_forFieldTypesOtherThanDate(){
        assertEquals("string", FieldType.STRING.transform("string"));
        assertEquals("int", FieldType.INT.transform("int"));
        assertEquals("long", FieldType.LONG.transform("long"));
        assertEquals("float", FieldType.FLOAT.transform("float"));
        assertEquals("double", FieldType.DOUBLE.transform("double"));
    }
}
