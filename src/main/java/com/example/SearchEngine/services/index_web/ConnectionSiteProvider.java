package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.NodePage;
import com.example.SearchEngine.models.answers.SiteAnswer;
import com.example.SearchEngine.repositories.index_web.PageRepo;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConnectionSiteProvider {
    private PageRepo pageRepo;

    @Autowired
    public ConnectionSiteProvider(PageRepo pageRepo) {
        this.pageRepo = pageRepo;
    }

    public Document getDoc(NodePage nodePage) {
        Document document = null;
        try {
            document = Jsoup.connect(nodePage.getPath()).maxBodySize(0).get();

        } catch (HttpStatusException ex) {
            pageRepo.savePageWithErrorCode(nodePage.getSuffix(), ex.getStatusCode(), nodePage);

            return document;
        } catch (IOException e) {
            pageRepo.savePageWithErrorCode(nodePage.getSuffix(), nodePage);
            return document;
        }
        return document;
    }

    public SiteAnswer getDoc(String path) {
        Document document = null;
        SiteAnswer siteAnswer = new SiteAnswer();
        try {
            document = Jsoup.connect(path).maxBodySize(0).get();

        } catch (HttpStatusException ex) {
            siteAnswer.setCode(ex.getStatusCode());
            siteAnswer.setException(ex);
            return siteAnswer;
        } catch (IOException e) {
            siteAnswer.setException(e);
            return siteAnswer;
        }
        siteAnswer.setDocument(document);

        return siteAnswer;
    }
}
