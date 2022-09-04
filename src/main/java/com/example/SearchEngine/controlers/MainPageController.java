package com.example.SearchEngine.controlers;

import com.example.SearchEngine.models.statistic.MainAnswerStatistic;
import com.example.SearchEngine.models.statistic.Statistics;
import com.example.SearchEngine.models.statistic.Total;
import com.example.SearchEngine.services.index_web.NodeService;
import com.example.SearchEngine.services.statistic.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainPageController {

    private final StatisticService statisticService;

    public MainPageController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @ModelAttribute("countIndexPages")
    public int getScanPages() {
        return NodeService.getUniqReffsCount();
    }

    @GetMapping("/admin")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/")
    public String getMainCustomPage() {
        return "index2";
    }

    @GetMapping("/statistics")
    @ResponseBody
    public MainAnswerStatistic getStatistics() {
        Statistics statistics = new Statistics();


        Total total = statisticService.getTotalStatistic();

        statistics.setDetailedList(statisticService.getDetailedAllSites());
        statistics.setTotal(total);

        MainAnswerStatistic mainAnswerStatistic = new MainAnswerStatistic();

        mainAnswerStatistic.setResult(true);
        mainAnswerStatistic.setStatistics(statistics);

        return mainAnswerStatistic;
    }


}
