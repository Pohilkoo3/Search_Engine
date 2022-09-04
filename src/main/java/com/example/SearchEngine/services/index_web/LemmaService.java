package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.Lemma;
import com.example.SearchEngine.repositories.lemmatizator.LemmaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LemmaService {

    private LemmaRepo lemmaRepo;

    @Autowired
    public LemmaService(LemmaRepo lemmaRepo) {
        this.lemmaRepo = lemmaRepo;
    }

    public void saveAllLemmas(List<Lemma> lemmaSet) {
        lemmaRepo.saveAllLemmas(lemmaSet);
    }

    public void saveLemma(Lemma lemma) {
        lemmaRepo.saveLemma(lemma);
    }

    public Lemma findLemmaByLemma(String key) {
        return lemmaRepo.findLemmaByLemma(key);
    }

    public Set<Integer> findPagesForLemma(int idLemma, int idSite) {
        return lemmaRepo.findPagesForLemma(idLemma, idSite);
    }

}
