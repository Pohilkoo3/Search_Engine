package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.Lemma;
import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.repositories.index_web.Site2LemmaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Site2LemmaService {

    private Site2LemmaRepo site2LemmaRepo;

    @Autowired
    public Site2LemmaService(Site2LemmaRepo site2LemmaRepo) {
        this.site2LemmaRepo = site2LemmaRepo;
    }

    public void saveNewSite2Lemma(Lemma lemma, int siteId) {
        site2LemmaRepo.saveNewSite2Lemma(lemma, siteId);
    }
}
