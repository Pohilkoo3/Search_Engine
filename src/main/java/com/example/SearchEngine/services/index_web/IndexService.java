package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.IndexEntity;
import com.example.SearchEngine.repositories.index_web.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private IndexRepo indexRepo;

    @Autowired
    public IndexService(IndexRepo indexRepo) {
        this.indexRepo = indexRepo;
    }

    public void save(IndexEntity indexEntity) {
        indexRepo.save(indexEntity);
    }

    public float findRankByLemmaAndPage(Integer pageId, Integer lemmaId) {
        return indexRepo.findRankByLemmaAndPage(pageId, lemmaId);
    }
}
