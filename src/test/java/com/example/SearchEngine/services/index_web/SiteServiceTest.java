package com.example.SearchEngine.services.index_web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@TestPropertySource("/application-test.properties")
class SiteServiceTest {
    int siteId;
    private SiteService siteService;

    @Autowired
    public SiteServiceTest(SiteService siteService) {
        this.siteService = siteService;
    }

    @BeforeEach
    void setUp() {
        siteId = 1;
    }

    @AfterEach
    void tearDown() {
        siteId = 0;
    }

    @Test
    void countPagesOnSite() {
        int count = siteService.countPagesOnSite(siteId);
        assertNotEquals(0, count);
        assertTrue(count > 0);
    }
}