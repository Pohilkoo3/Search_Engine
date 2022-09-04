package com.example.SearchEngine.interfaces.index_web;

import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.models.index_web.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteInterface extends JpaRepository <SiteEntity, Integer> {

    @Query(value = "SELECT count(*) FROM page", nativeQuery = true)
    int countAllPagesInDB();

    @Query(value = "SELECT count(*) FROM page p WHERE p.site_id=(SELECT s.id FROM site s WHERE s.uri=?1)", nativeQuery = true)
    int countPagesOnSiteByPath(String path);

    @Query(value = "SELECT count(*) FROM page p where p.site_id=?1", nativeQuery = true)
    int countPagesOnSite(int siteId);

    @Query(value = "SELECT count(*) FROM lemma l JOIN site2lemma u ON l.id=u.lemma_id JOIN site s ON u.site_id=s.id where s.uri=?1", nativeQuery = true)
    int countLemmasOnSite(String path);

    SiteEntity findByUri(String path);

    @Query(value = "SELECT count(*) FROM site", nativeQuery = true)
    int countAllSites();

    int countAllByStatus(Status status);




}
