package org.motechproject.export.model;

import org.junit.Test;
import org.motechproject.export.annotation.ComponentTypeProvider;
import org.motechproject.export.annotation.ExportValue;

import java.util.ArrayList;
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
        List<String> columnHeaders = new ExportDataModel(dummy2.class).columnHeaders();
        assertEquals(2,columnHeaders.size());
    }
}

class dummy2{

    private String district;
    private String panchy;

    @ExportValue(column="district", index = 0)
    public String getDistrict() {
        return district;
    }

    @ExportValue(column="panchy", index = 1)
    public String getPanchy() {
        return panchy;
    }

}
