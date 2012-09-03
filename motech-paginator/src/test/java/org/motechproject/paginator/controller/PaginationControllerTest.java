package org.motechproject.paginator.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.repository.AllPagingServices;
import org.motechproject.paginator.service.Paging;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PaginationControllerTest {

    @Mock
    AllPagingServices allPagingServices;
    PaginationController paginationController;

    @Before
    public void setup() {
        initMocks(this);
        paginationController = new PaginationController(allPagingServices);
    }

    @Test
    public void shouldPageEntity() throws Exception {
        Paging pagingService = mock(Paging.class);
        when(allPagingServices.getPagingServiceFor("entity1")).thenReturn(pagingService);

        standaloneSetup(paginationController).build()
                .perform(get("/page/entity1").param("pageNo", "1"))
                .andExpect(status().isOk());
        verify(pagingService).page(eq(1), anyInt());
    }
}
