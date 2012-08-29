package org.motechproject.paginator.controller;

import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping(value = "/page")
public class PaginationController {

    @Value( value = "${paginator.rowsPerPage:10}")
    private String rowsPerPage;

    @Resource(name = "pagingServiceMap")
    Map<String, Paging> pagingServiceMap;

    @RequestMapping(method = RequestMethod.GET, value = "/{entity}")
    @ResponseBody
    public PageResults page(@PathVariable String entity, @RequestParam(defaultValue = "1") String pageNo) {
        if(pagingServiceMap.containsKey(entity)){
            PageResults results = pagingServiceMap.get(entity).page(parseInt(pageNo), parseInt(rowsPerPage));
            return results;
        }
        return null;
    }
}
