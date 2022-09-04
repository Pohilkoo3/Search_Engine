package com.example.SearchEngine.services.index_web;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Service
public class RemoveService {

    private SiteService siteService;
    private EntityManagerFactory entityManagerFactory;


    @Autowired
    public RemoveService(SiteService siteService, EntityManagerFactory entityManagerFactory) {
        this.siteService = siteService;
        this.entityManagerFactory = entityManagerFactory;

    }

    public void deleteAllSites() {
        deleteInfoInDataBase();
        deleteInfoInUniqReffs();
    }

    private void deleteInfoInUniqReffs() {
        NodeService.clearUniqReffs();
    }

    private void deleteInfoInDataBase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            String sql = String.format("TRUNCATE TABLE lemma Cascade; TRUNCATE TABLE page Cascade;TRUNCATE TABLE site Cascade;TRUNCATE TABLE site2lemma Cascade;");
            session.createSQLQuery(sql).executeUpdate();
            tx.commit();

        } catch (HibernateException hex) {
            if (tx != null) {
                tx.rollback();
            } else {
                hex.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

}
