package com.example.SearchEngine.interfaces.index_web;

import com.example.SearchEngine.models.index_web.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LemmaInterface extends JpaRepository<Lemma, IndexInterface>{

    Lemma findByLemma(String lemma);

    @Query(value = "SELECT page_id FROM index i JOIN page p ON p.id=i.page_id where i.lemma_id=?1 AND p.site_id=?2", nativeQuery = true)
    Set<Integer> findPagesForLemmaOnSite(int idLemma, int idSite);

    @Query(value = "SELECT page_id FROM index where index.lemma_id=?1", nativeQuery = true)
    Set<Integer> findPagesForLemmaOnAllSites(int idLemma);
    @Query(value = "SELECT count(*) FROM lemma", nativeQuery = true)
    int countAllLemmas();
}
