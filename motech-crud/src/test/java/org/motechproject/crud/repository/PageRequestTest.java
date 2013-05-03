package org.motechproject.crud.repository;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PageRequestTest {

    @Test
    public void shouldReturnPageNumber(){
        PageRequest firstPage = new PageRequest(0, 10);
        assertEquals(1 , firstPage.getPageNumber());


        PageRequest thirdPage = new PageRequest(20, 10);
        assertEquals(3, thirdPage.getPageNumber());
        assertEquals(20, thirdPage.getOffset());
        assertEquals(10, thirdPage.getPageSize());
    }
}
