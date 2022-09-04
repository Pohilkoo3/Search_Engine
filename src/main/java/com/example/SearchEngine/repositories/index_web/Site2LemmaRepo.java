package com.example.SearchEngine.repositories.index_web;

import com.example.SearchEngine.interfaces.index_web.Site2LemmaInterface;
import com.example.SearchEngine.models.index_web.Lemma;
import com.example.SearchEngine.models.index_web.Site2Lemma;
import com.example.SearchEngine.models.index_web.SiteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Site2LemmaRepo {

    @Autowired
    private Site2LemmaInterface site2LemmaInterface;

    public void saveNewSite2Lemma(Lemma lemma, int siteId) {
        Site2Lemma site2Lemma = new Site2Lemma();
        site2Lemma.setSite_id(siteId);
        site2Lemma.setLemmaId(lemma.getId());
        site2Lemma.setId(lemma.getId() + " " + siteId);
        site2LemmaInterface.save(site2Lemma);
    }
}
