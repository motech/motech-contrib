package org.motechproject.web.context;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class HttpThreadContextTest {
    @Test
    public void shouldWorkWithThreadContext(){
        String contextVar = "string";

        HttpThreadContext.set(contextVar);

        assertEquals(contextVar, HttpThreadContext.get());

        HttpThreadContext.unset();

        assertNull(HttpThreadContext.get());
    }
}
