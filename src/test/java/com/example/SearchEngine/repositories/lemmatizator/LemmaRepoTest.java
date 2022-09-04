package com.example.SearchEngine.repositories.lemmatizator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LemmaRepoTest {
 private LemmaRepo lemmaRepo;
    int idLemma;
    int idSite;
    int idSite2;


 @Autowired
    public LemmaRepoTest(LemmaRepo lemmaRepo) {
        this.lemmaRepo = lemmaRepo;
    }

    @BeforeEach
    void setUp() {
     idLemma = 5;
     idSite = 2;
     idSite2 = 0;
    }

    @AfterEach
    void tearDown() {
        idLemma = 0;
        idSite = 0;
    }

    @Test
    void findPagesForLemmaOnSite() {
     Set<Integer> setPagesId = lemmaRepo.findPagesForLemma(idLemma, idSite);
     assertEquals(6, setPagesId.size());

    }

    @Test
    void findPagesForLemmaAllSitesSite() {
        Set<Integer> setPagesId = lemmaRepo.findPagesForLemma(idLemma, idSite2);
        assertEquals(11, setPagesId.size());

    }
}