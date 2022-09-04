package com.example.SearchEngine.repositories.index_web;

import com.example.SearchEngine.interfaces.index_web.PageInterface;
import com.example.SearchEngine.models.index_web.NodePage;
import com.example.SearchEngine.models.index_web.PageEntity;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageRepo {
    @Autowired
    private PageInterface pageInterface;


    public PageEntity savePageFromDocument(Document document, String suffix, int siteId) {
        int responseStatus = document.connection().response().statusCode();
        PageEntity page = new PageEntity();
        page.setPath(suffix.isEmpty()? "/" : suffix);
        page.setCodeResponse(responseStatus);
        page.setContent(document.html());
        page.setSite_id(siteId);
        return pageInterface.save(page);
    }

    public void savePageWithErrorCode(String path, int code, NodePage nodePage){
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCodeResponse(code);
        pageEntity.setPath(path);
        pageEntity.setSite_id(nodePage.getSiteId());
        pageInterface.save(pageEntity);

    }
    public void savePageWithErrorCode(String path, NodePage nodePage){
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCodeResponse(404);
        pageEntity.setPath(path);
        pageEntity.setSite_id(nodePage.getSiteId());
        pageInterface.save(pageEntity);

    }

//    public void getPersistPage(){
//        PageEntity page = new PageEntity();
//
//    }

    public PageEntity getPageById(Integer idPage) {
        return pageInterface.findById((long) idPage).orElse(null);
    }

    public int getCountPages(){
        return pageInterface.countAllPages();
    }


}
