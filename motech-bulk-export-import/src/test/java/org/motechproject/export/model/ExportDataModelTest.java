package org.motechproject.export.model;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: vishnukarthik
 * Date: 11/9/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportDataModelTest {

    @Test
    public void shouldReturnColumnHeaders(){
        List<String> columnHeaders = new ExportDataModel(DummyCsv.class).columnHeaders();
        assertEquals(2,columnHeaders.size());
    }
}

