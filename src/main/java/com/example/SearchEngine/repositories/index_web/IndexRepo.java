package com.example.SearchEngine.repositories.index_web;

import com.example.SearchEngine.interfaces.index_web.IndexInterface;
import com.example.SearchEngine.models.index_web.IndexEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexRepo {
    @Autowired
    private IndexInterface indexInterface;

    public void save(IndexEntity indexEntity) {
        indexInterface.save(indexEntity);
    }

    public float findRankByLemmaAndPage(Integer pageId, Integer lemmaId) {
        return indexInterface.findIndexByLemmaIdAndPageId(pageId, lemmaId);
    }
}
