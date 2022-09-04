package com.example.SearchEngine.interfaces.index_web;

import com.example.SearchEngine.models.index_web.IndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IndexInterface extends JpaRepository<IndexEntity, Integer> {

    @Query(value = "SELECT i.rank from index i JOIN page p ON p.id=i.page_id JOIN lemma l ON l.id=i.lemma_id WHERE p.id=?1 AND l.id=?2", nativeQuery = true)
    float findIndexByLemmaIdAndPageId(int pageId, int idLemma);
}
