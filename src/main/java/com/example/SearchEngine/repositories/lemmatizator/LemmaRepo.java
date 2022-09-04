package com.example.SearchEngine.repositories.lemmatizator;

import com.example.SearchEngine.interfaces.index_web.LemmaInterface;
import com.example.SearchEngine.models.index_web.Lemma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LemmaRepo {
    @Autowired
    private LemmaInterface lemmaInterface;


    public void saveAllLemmas(List<Lemma> lemmaSet) {
        lemmaInterface.saveAll(lemmaSet);
    }

    public void saveLemma(Lemma lemma) {
        lemmaInterface.save(lemma);
    }

    public Lemma findLemmaByLemma(String key) {
       Lemma lemma = lemmaInterface.findByLemma(key);
       return lemma == null? new Lemma("null") : lemma;
    }

    public Set<Integer> findPagesForLemma(int idLemma, int idSite) {
       return idSite == 0?  lemmaInterface.findPagesForLemmaOnAllSites(idLemma) : lemmaInterface.findPagesForLemmaOnSite(idLemma, idSite);
    }

    public int getCountLemmas(){
        return lemmaInterface.countAllLemmas();
    }

}
