package com.example.SearchEngine.interfaces.index_web;

import com.example.SearchEngine.models.index_web.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PageInterface extends JpaRepository<PageEntity, Long> {


    @Query(value = "SELECT count(*) FROM page", nativeQuery = true)
    int countAllPages();
}
