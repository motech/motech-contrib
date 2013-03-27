package org.motechproject.calllog.repository;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HstoreHelperTest {

    @Test
    public void shouldConvertTheGivenMapIntoKeyValuePairString() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        String combinedString = HstoreHelper.toString(map);

        assertThat(combinedString, is("\"k1\"=>\"v1\", \"k2\"=>\"v2\", \"k3\"=>\"v3\""));
    }

    @Test
    public void shouldReturnEmptyStringGivenAnEmptyMap() {
        Map<String, String> map = new HashMap<>();

        String combinedString = HstoreHelper.toString(map);

        assertThat(combinedString, is(""));
    }

    @Test
    public void shouldConvertACombinedStringIntoMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("k1", "v1");
        expectedMap.put("k2", "v2");
        expectedMap.put("k3", "v3");

        Map<String, String> map = HstoreHelper.toMap("\"k1\"=>\"v1\", \"k2\"=>\"v2\", \"k3\"=>\"v3\"");
        assertThat(map, is(expectedMap));
    }

    @Test
    public void shouldReturnEmptyMapIfStringIsEmpty() {
        Map<String, String> map = HstoreHelper.toMap("");

        assertTrue(map.isEmpty());
    }
}
