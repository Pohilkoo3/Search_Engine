package com.example.SearchEngine.services.statistic;

import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.models.statistic.Detailed;
import com.example.SearchEngine.models.statistic.Total;
import com.example.SearchEngine.repositories.index_web.PageRepo;
import com.example.SearchEngine.repositories.index_web.SiteRepo;
import com.example.SearchEngine.repositories.lemmatizator.LemmaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StatisticService {

    private SiteRepo siteRepo;
    private PageRepo pageRepo;
    private LemmaRepo lemmaRepo;


    @Autowired
    public StatisticService(SiteRepo siteRepo, PageRepo pageRepo, LemmaRepo lemmaRepo) {
        this.siteRepo = siteRepo;
        this.pageRepo = pageRepo;
        this.lemmaRepo = lemmaRepo;
    }

    public Total getTotalStatistic(){
        Total total = new Total();
        total.setSites(siteRepo.getCountSites());
        total.setPages(pageRepo.getCountPages());
        total.setLemmas(lemmaRepo.getCountLemmas());
        total.setIndexing(siteRepo.isIndexing());
        return total;
    }

    public HashSet<Detailed> getDetailedAllSites(){
        HashSet<Detailed> detailedHashSet = new HashSet<>(0);
        List<SiteEntity> setAllSites = siteRepo.getListSites();
        if (!setAllSites.isEmpty()){
            for (SiteEntity site : setAllSites) {
                Detailed detailed = new Detailed();
                detailed.setUrl(site.getUri());
                detailed.setName(site.getName());
                detailed.setStatus(site.getStatus());
                detailed.setStatusTime(site.getStatusTime());
                detailed.setError(site.getLastError());
                detailed.setPages(siteRepo.countPagesOnSite(site.getId()));
                detailed.setLemmas(siteRepo.countLemmaOnSite(site.getUri()));
                detailedHashSet.add(detailed);
            }
        }
        return detailedHashSet;
    }


}
