package org.motechproject.export.controller;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ExportControllerTest {

    @Test
    public void shouldForwardToErrorPageOnException() throws Exception {
        ExportController controller = new ExportController() {
            @RequestMapping(value = "/test", method = RequestMethod.GET)
            public String throwException() {
                throw new RuntimeException("Expected exception");
            }
        };

        standaloneSetup(controller).build()
                .perform(get("/reports/test"))
                .andExpect(content().string("Error occurred while exporting."));
    }

}
