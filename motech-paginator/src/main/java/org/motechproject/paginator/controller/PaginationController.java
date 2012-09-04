package org.motechproject.paginator.controller;

import org.motechproject.paginator.repository.AllPagingServices;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping(value = "/page")
public class PaginationController {

    private String rowsPerPage = "10";

    private AllPagingServices allPagingServices;

    @Autowired
    public PaginationController(AllPagingServices allPagingServices) {
        this.allPagingServices = allPagingServices;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entity}")
    @ResponseBody
    public PageResults page(@PathVariable String entity, @RequestParam(defaultValue = "1") String pageNo) {
        PageResults results = null;
        Paging pagingService = allPagingServices.getPagingServiceFor(entity);

        if (pagingService != null)
            results = pagingService.page(parseInt(pageNo), parseInt(rowsPerPage));

        return results;
    }
}
