package org.motechproject.paginator.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.paginator.repository.AllPagingServices;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Properties;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping(value = "/page")
public class PaginationController {

    private AllPagingServices allPagingServices;

    @Autowired
    public PaginationController(AllPagingServices allPagingServices) {
        this.allPagingServices = allPagingServices;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entity}")
    @ResponseBody
    public PageResults page(@PathVariable String entity,
                            @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
                            @RequestParam("rowsPerPage") String rowsPerPage,
                            @RequestParam(value = "searchCriteria", required = false, defaultValue = "{}") String searchCriteria) throws IOException {
        Paging pagingService = getPagingService(entity);
        Properties criteria = createCriteria(searchCriteria);
        return fetchData(pageNo, rowsPerPage, criteria, pagingService);
    }

    private PageResults fetchData(String pageNo, String rowsPerPage, Properties criteria, Paging pagingService) {
        return (pagingService != null) ? pagingService.page(parseInt(pageNo), parseInt(rowsPerPage), criteria) : null;
    }

    private Properties createCriteria(String searchCriteria) throws IOException {
        return new ObjectMapper().readValue(searchCriteria, Properties.class);
    }

    private Paging getPagingService(String entity) {
        Paging pagingService = allPagingServices.getPagingServiceFor(entity);
        if (pagingService != null) {
            return pagingService;
        } else {
            throw new RuntimeException("No service found which provides paging for " + entity);
        }
    }
}
