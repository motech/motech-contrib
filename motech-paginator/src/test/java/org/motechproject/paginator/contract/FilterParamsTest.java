package org.motechproject.paginator.contract;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FilterParamsTest {

    @Test
    public void shouldRemoveEmptyParams() {
        FilterParams filterParams = new FilterParams();
        filterParams.put("keyWithEmptyValue", "");
        filterParams.put("keyWithBlankValue", " ");
        filterParams.put("keyWithValue", "abcd");

        FilterParams updatedFilterParams = filterParams.removeEmptyParams();
        assertNull(updatedFilterParams.get("keyWithEmptyValue"));
        assertNull(updatedFilterParams.get("keyWithEmptyValue"));
        assertEquals("abcd", updatedFilterParams.get("keyWithValue"));
    }
}
