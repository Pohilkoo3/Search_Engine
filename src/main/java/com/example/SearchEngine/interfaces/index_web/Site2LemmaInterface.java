package com.example.SearchEngine.interfaces.index_web;

import com.example.SearchEngine.models.index_web.Site2Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Site2LemmaInterface extends JpaRepository<Site2Lemma, Integer> {
}
