package com.example.SearchEngine.repositories.index_web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@TestPropertySource("/application-test.properties")
class SiteRepoTest {

    private SiteRepo siteRepo;

    @Autowired
    public SiteRepoTest(SiteRepo siteRepo) {
        this.siteRepo = siteRepo;
    }



    @Test
    void getCountSites() {
       assertNotNull(siteRepo.getCountSites());
       assertEquals(2, siteRepo.getCountSites());
    }

    @Test
    void isIndexing() {
       assertTrue(siteRepo.isIndexing());
    }

    @Test
    void countAllPagesInDB() {
        assertEquals(65, siteRepo.countAllPagesInDB());
    }
}