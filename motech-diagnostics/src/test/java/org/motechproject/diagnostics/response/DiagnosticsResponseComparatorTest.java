package org.motechproject.diagnostics.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiagnosticsResponseComparatorTest {
    @Test
    public void shouldCompareTwoResponseBasedOnNames() {
        DiagnosticsResponseComparator diagnosticsResponseComparator = new DiagnosticsResponseComparator();

        DiagnosticsResponse response1 = new DiagnosticsResponse("aa", null);
        DiagnosticsResponse response2 = new DiagnosticsResponse("bb", null);
        DiagnosticsResponse responseWithNullName = new DiagnosticsResponse(null, null);

        assertEquals(-1, diagnosticsResponseComparator.compare(response1, response2));
        assertEquals(1, diagnosticsResponseComparator.compare(response2, response1));
        assertEquals(0, diagnosticsResponseComparator.compare(response1, response1));

        assertEquals(-1, diagnosticsResponseComparator.compare(null, response1));
        assertEquals(0, diagnosticsResponseComparator.compare(null, null));
        assertEquals(1, diagnosticsResponseComparator.compare(response1, null));

        assertEquals(-1, diagnosticsResponseComparator.compare(responseWithNullName, response1));
        assertEquals(0, diagnosticsResponseComparator.compare(responseWithNullName, responseWithNullName));
        assertEquals(1, diagnosticsResponseComparator.compare(response1, responseWithNullName));
    }


}
