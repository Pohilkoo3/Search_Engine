package com.example.SearchEngine.repositories.index_web;

import com.example.SearchEngine.interfaces.index_web.SiteInterface;
import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.models.index_web.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SiteRepo {

    @Autowired
    private SiteInterface siteInterface;


    public void saveSite(SiteEntity siteEntity) {
        siteInterface.save(siteEntity);
    }

    public int countAllPagesInDB(){
        return siteInterface.countAllPagesInDB();
    }
    public int countPagesOnSite(int siteId){
        return siteInterface.countPagesOnSite(siteId);
    }

    public int countLemmaOnSite(String path){
        return siteInterface.countLemmasOnSite(path);
    }

    public SiteEntity getSiteIdByPath(String pathSite) {
        return siteInterface.findByUri(pathSite);
    }
    public int getCountSites(){
        return siteInterface.countAllSites();
    }

    public boolean isIndexing(){
        return siteInterface.countAllByStatus(Status.INDEXING) != 0;
    }

    public List<SiteEntity> getListSites() {
      return siteInterface.findAll();

    }

    public SiteEntity getSiteById(int id) {
        return siteInterface.findById(id).orElse(null);
    }


}
