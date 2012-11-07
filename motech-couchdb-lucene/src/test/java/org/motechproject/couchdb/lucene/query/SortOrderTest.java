package org.motechproject.couchdb.lucene.query;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortOrderTest{

    @Test
    public void shouldGetSortOrderForGivenString() {
        assertEquals(SortOrder.ASC, SortOrder.getSortOrder("ASC"));
        assertEquals(SortOrder.DESC, SortOrder.getSortOrder("Desc"));
        assertEquals(SortOrder.ASC, SortOrder.getSortOrder("junk"));
    }
}
