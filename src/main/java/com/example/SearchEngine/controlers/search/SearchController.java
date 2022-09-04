package com.example.SearchEngine.controlers.search;

import com.example.SearchEngine.exceptions.IllegalSearchQueryParamException;
import com.example.SearchEngine.models.search.DataResponse;
import com.example.SearchEngine.models.search.ResponseSearch;
import com.example.SearchEngine.services.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    private ResponseSearch responseSearch;
    private DataResponse dataResponse;
    private SearchService searchService;
    @Value("${percent.ancought.lemmas.frequence}")
    private int percentFrequencyLemma;

    @Autowired
    public SearchController(ResponseSearch responseSearch, DataResponse dataResponse, SearchService searchService) {
        this.responseSearch = responseSearch;
        this.dataResponse = dataResponse;
        this.searchService = searchService;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseSearch getSearchResult(@RequestParam("query")String query, @RequestParam(value = "site", required = false) String site,
                                          @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) throws IllegalSearchQueryParamException {
        return searchService.getResponseSearch(query, site,percentFrequencyLemma, offset, limit);
    }

}
