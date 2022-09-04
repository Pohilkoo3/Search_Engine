package com.example.SearchEngine.controlers.index_web;

import com.example.SearchEngine.models.answers.ResponseAddPageInIndex;
import com.example.SearchEngine.models.errors.InterruptedCustomError;
import com.example.SearchEngine.models.payload.Url;
import com.example.SearchEngine.repositories.index_web.PageRepo;
import com.example.SearchEngine.repositories.index_web.SiteRepo;
import com.example.SearchEngine.runtime.index_web.RunIndexingProvider;
import com.example.SearchEngine.services.index_web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {
    private RunIndexingProvider runIndexingProvider;
    private NodeService nodeService;
    private EntityManagerFactory entityManagerFactory;
    private SiteRepo siteRepo;

    private RemoveService removeService;


    @Value("${path.page.all2}")
    private String path;
    @Value("${time.sleep.between.request}")
    private int timeBetweenRequest;

    @Autowired
    public IndexController(RunIndexingProvider runIndexingProvider, NodeService nodeService, EntityManagerFactory entityManagerFactory, SiteRepo siteRepo, RemoveService removeService) {
        this.runIndexingProvider = runIndexingProvider;
        this.nodeService = nodeService;
        this.entityManagerFactory = entityManagerFactory;
        this.siteRepo = siteRepo;
        this.removeService = removeService;
    }

    @ModelAttribute("countIndexPages")
    public int getScanPages(){
        return  NodeService.getUniqReffsCount();
    }

    @GetMapping("/startIndexing")
    public String getIndexingPage(Model model) {
        removeService.deleteAllSites();
        String[] paths = path.split(",");
        Set<Thread> listTread = new HashSet<>(0);
        for (String pathToSite : paths) {
            Runnable task = () -> {
                runIndexingProvider.run(pathToSite, timeBetweenRequest);
            };
            Thread thread = new Thread(task, pathToSite + " Thread");
            thread.start();
            listTread.add(thread);
            System.out.println("Thread search - started " + thread.getName());
        }
        listTread.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Scan finished!");
        return "redirect:/";
    }

    @GetMapping("/stopIndexing")
    public String getIndexingStop(Model model) throws InterruptedCustomError {

        model.addAttribute("isInterrupt", true);
        model.addAttribute("messageInterrupt", "Сканирование завершено пользователем.");
        runIndexingProvider.stopIndexing();
        runIndexingProvider = new RunIndexingProvider(entityManagerFactory, nodeService, new ConnectionSiteProvider(new PageRepo()),
                new SiteService(siteRepo));
        return "redirect:/";
    }

    @PostMapping("/indexPage")
    @ResponseBody
    public ResponseAddPageInIndex addNewPageInIndex (Url url){
        String[] pathElements = url.getUrl().split("/");
        String prefix = pathElements[0] + "//" + pathElements[1] + pathElements[2];
        String suffix = url.getUrl().replaceAll(prefix, "");
        String[] sitesAddress = path.split(",");
        ResponseAddPageInIndex response = new ResponseAddPageInIndex();

        if (Arrays.stream(sitesAddress).filter(a -> a.equals(prefix)).count() == 0){
            response.setResult(false);
            response.setError("Данная страница находится за пределами сайтов, \n" +
                    "указанных в конфигурационном файле\n");
        } else {
            response = runIndexingProvider.runNewPage(prefix, suffix, url.getUrl());
        }
        return response;
    }


}
