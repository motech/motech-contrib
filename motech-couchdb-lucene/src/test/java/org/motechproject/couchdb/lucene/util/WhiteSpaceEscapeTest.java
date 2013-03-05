package org.motechproject.couchdb.lucene.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WhiteSpaceEscapeTest {

    @Test
    public void shouldRemoveSpacesFromQueryParams() {
        WhiteSpaceEscape whiteSpaceEscape = new WhiteSpaceEscape();

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("Key", "Value With Spaces");

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("Key", "Value\\ With\\ Spaces");

        Map<String, Object> escapedQueryParams = whiteSpaceEscape.escape(queryParams);
        assertThat(escapedQueryParams, is(expectedParams));
        assertThat((String) queryParams.get("Key"), is("Value With Spaces"));
    }
}
