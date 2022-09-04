package com.example.SearchEngine.runtime.index_web;

import com.example.SearchEngine.models.answers.ResponseAddPageInIndex;
import com.example.SearchEngine.models.answers.SiteAnswer;
import com.example.SearchEngine.models.index_web.NodePage;
import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.models.index_web.Status;
import com.example.SearchEngine.services.index_web.ConnectionSiteProvider;
import com.example.SearchEngine.services.index_web.NodeService;
import com.example.SearchEngine.services.index_web.RecursiveGetAllLinksFromPage;
import com.example.SearchEngine.services.index_web.SiteService;
import com.example.SearchEngine.utils.ForkJoinUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


@Component
public class RunIndexingProvider {

    private EntityManagerFactory entityManagerFactory;
    private NodeService nodeService;
    private ConnectionSiteProvider connectionSiteProvider;
    private ForkJoinPool forkJoinPool;

    private SiteService siteService;


    @Autowired
    public RunIndexingProvider(EntityManagerFactory entityManagerFactory, NodeService nodeService,
                               ConnectionSiteProvider connectionSiteProvider, SiteService siteService) {
        this.entityManagerFactory = entityManagerFactory;
        this.nodeService = nodeService;
        this.connectionSiteProvider = connectionSiteProvider;

        this.siteService = siteService;
        forkJoinPool = ForkJoinUtil.forkJoinPool;
    }


    public void run(String path, Integer timeBetweenRequest) {
        SiteAnswer siteAnswer = connectionSiteProvider.getDoc(path);
        Document document = siteAnswer.getDocument();
        SiteEntity siteEntity = new SiteEntity();
        siteEntity.setUri(path);
        siteEntity.setStatusTime(LocalDateTime.now());
        if (document == null) {
            siteEntity.setStatus(Status.FAILED);
            siteEntity.setLastError(siteAnswer.getException().getMessage() + " -> " + "Io Exception");
            siteEntity.setName("No name. Indexing failed.");
            siteService.saveSite(siteEntity);
            return;
        }
        siteEntity.setName(document.getElementsByTag("title").text());
        siteEntity.setStatus(Status.INDEXING);

        siteService.saveSite(siteEntity);


        NodePage nodePage = new NodePage(path);
        nodePage.setPrefix(path);
        nodePage.setSuffix("");
        nodePage.setTimeBetweenRequest(timeBetweenRequest);
        nodePage.setSiteId(siteEntity.getId());


        RecursiveGetAllLinksFromPage recursiveGetAllLinksFromPage = new RecursiveGetAllLinksFromPage(nodePage, nodeService);
        forkJoinPool.invoke(recursiveGetAllLinksFromPage);

        siteEntity.setStatus(Status.INDEXED);
        siteService.saveSite(siteEntity);
        System.out.println("End of scanning " + path + " => " + Thread.currentThread().getName());

    }

    public void stopIndexing() {
        try {
            forkJoinPool.shutdown();
            forkJoinPool.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        } finally {
            forkJoinPool.shutdownNow();
            ForkJoinUtil.refreshForkJoinPool();
        }
    }

    public ResponseAddPageInIndex runNewPage(String prefix, String suffix, String pathPage) {
        SiteEntity site = siteService.findSiteIdByPath(prefix);
        NodePage nodePage = new NodePage(pathPage);
        nodePage.setPrefix(prefix);
        nodePage.setSuffix(suffix);
        nodePage.setPath(pathPage);
        nodePage.setTimeBetweenRequest(0);
        nodePage.setSiteId(site.getId());

        ResponseAddPageInIndex responseAddPageInIndex = nodeService.handlerSinglePage(nodePage);

        System.out.println("End of scanning " + pathPage + " => " + Thread.currentThread().getName());
        return responseAddPageInIndex;
    }
}
