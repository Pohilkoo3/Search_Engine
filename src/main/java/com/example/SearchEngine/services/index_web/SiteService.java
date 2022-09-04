package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.repositories.index_web.SiteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    private SiteRepo siteRepo;


    @Autowired
    public SiteService(SiteRepo siteRepo) {
        this.siteRepo = siteRepo;
    }

    public void saveSite(SiteEntity siteEntity) {
        siteRepo.saveSite(siteEntity);
    }

    public int countAllPagesInDB(){
       return siteRepo.countAllPagesInDB();
    }

    public int countPagesOnSite(int siteId){
        return siteRepo.countPagesOnSite(siteId);
    }

    public SiteEntity findSiteIdByPath(String pathSite) {
       return siteRepo.getSiteIdByPath(pathSite);
    }

    public SiteEntity getSiteById(int id){
     return siteRepo.getSiteById(id);
    }

}
