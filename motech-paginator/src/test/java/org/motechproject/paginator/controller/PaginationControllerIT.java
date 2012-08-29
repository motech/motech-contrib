package org.motechproject.paginator.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-applicationContext-Paginator.xml"})
public class PaginationControllerIT {

    @Autowired
    PaginationController paginationController;

    @Autowired
    @Qualifier(value = "pagingService1")
    Paging pagingService1;

    @Autowired
    @Qualifier(value = "pagingService2")
    Paging pagingService2;

    @Test
    public void shouldAutowirePaginationController() throws Exception {
        PageResults pageResults = mock(PageResults.class);
        when(pagingService1.page(1, 10)).thenReturn(pageResults);

        standaloneSetup(paginationController).build()
                .perform(get("/page/entity1").param("pageNo", "1"));
                //.andExpect(status().isOk());

        verify(pagingService1).page(1, 10);
    }


}
