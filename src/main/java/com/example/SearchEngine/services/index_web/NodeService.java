package com.example.SearchEngine.services.index_web;

import com.example.SearchEngine.models.answers.ResponseAddPageInIndex;
import com.example.SearchEngine.models.index_web.*;
import com.example.SearchEngine.repositories.index_web.PageRepo;
import com.example.SearchEngine.services.lucene_morphology.LuceneMorphologyService;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class NodeService {
    private ConnectionSiteProvider connectionSiteProvider;

    private LemmaService lemmaService;
    private IndexService indexService;
    private Site2LemmaService site2LemmaService;
    private LuceneMorphologyService luceneMorphologyService;
    private static CopyOnWriteArraySet<String> uniqReffs;
    private Logger logger = LogManager.getLogger("Search");
    private static Pattern patternSearchDot;
    private SiteService siteService;
    private PageRepo pageRepo;

    private EntityManagerFactory entityManagerFactory;


    static {
        uniqReffs = new CopyOnWriteArraySet<>();
        patternSearchDot = Pattern.compile(".*\\.[^html]");

    }

    @Autowired
    public NodeService(EntityManagerFactory entityManagerFactory, ConnectionSiteProvider connectionSiteProvider,
                       LemmaService lemmaService, LuceneMorphologyService luceneMorphologyService,
                       IndexService indexService,
                       Site2LemmaService site2LemmaService, SiteService siteService,
                       PageRepo pageRepo) {
        this.connectionSiteProvider = connectionSiteProvider;
        this.lemmaService = lemmaService;
        this.luceneMorphologyService = luceneMorphologyService;
        this.indexService = indexService;

        this.site2LemmaService = site2LemmaService;
        this.siteService = siteService;
        this.entityManagerFactory = entityManagerFactory;
        this.pageRepo = pageRepo;
    }

    public void handlerPage(NodePage nodePage) {
        System.out.println("Try fetch doc from " + nodePage.getPath());

        //забираем документ. Доделать возарвт из  метода документа и сохранение ответа
        Document document = connectionSiteProvider.getDoc(nodePage);
        if (document == null) {
            System.out.println("Page is not available");
            return;
        }

        //сохраняем страницу
        PageEntity page = pageRepo.savePageFromDocument(document, nodePage.getSuffix(), nodePage.getSiteId());

//        nodePage.getSite().addPageToSite(page);



        // Достаем леммы из title && body
        String[] titleAndBody = getTitleAndBody(document);
        Map<String, Integer> titleLemmas = luceneMorphologyService.collectLemmas(titleAndBody[0]);
        Map<String, Integer> bodyLemmas = luceneMorphologyService.collectLemmas(titleAndBody[1]);
        //Соединяем тайтл и боди в одну хэш мэп для подсчета ранга
        Map<String, Float> totalWords = new HashMap<>();
        bodyLemmas.entrySet().stream().forEach(w -> totalWords.put(w.getKey(), w.getValue() * 0.8f));
        titleLemmas.entrySet().stream().forEach(w -> totalWords.put(w.getKey(), w.getValue() + totalWords.getOrDefault(w.getKey(), 0f)));


        saveLemma(totalWords, page, nodePage);


//проверяем на уникальность и если нет еще в общем листе ссылок, то добавляем в уник и к детям этого узла
        HashSet<String> unigRefInNode = new HashSet<>();
        HashSet<String> allLinks = getAllLinks(document);
        for (String ref : allLinks) {
            Matcher matcher = patternSearchDot.matcher(ref);
            String fullPath = nodePage.getPrefix() + ref;
            if (!uniqReffs.contains(fullPath) && !matcher.find()) {
                uniqReffs.add(fullPath);
                unigRefInNode.add(ref);
                logger.info(fullPath);
            }
        }
        nodePage.setRefOnChilds(unigRefInNode);
        System.out.println("Count of uniqReff = " + uniqReffs.size());
    }

    public ResponseAddPageInIndex handlerSinglePage(NodePage nodePage){
        System.out.println("Try fetch doc from " + nodePage.getPath());
        ResponseAddPageInIndex response = new ResponseAddPageInIndex();
        //забираем документ. Доделать возарвт из  метода документа и сохранение ответа
        Document document = connectionSiteProvider.getDoc(nodePage);
        if (document == null) {
            response.setResult(false);
            response.setError("Страница по запрошенному адресу недоступна.");
            return response;
        }
/**Одинаковый код с предыдущим методом вынести в отдельный метод**/
        //сохраняем страницу
        PageEntity page = pageRepo.savePageFromDocument(document, nodePage.getSuffix(), nodePage.getSiteId());

        // Достаем леммы из title && body
        String[] titleAndBody = getTitleAndBody(document);
        Map<String, Integer> titleLemmas = luceneMorphologyService.collectLemmas(titleAndBody[0]);
        Map<String, Integer> bodyLemmas = luceneMorphologyService.collectLemmas(titleAndBody[1]);
        //Соединяем тайтл и боди в одну хэш мэп для подсчета ранга
        Map<String, Float> totalWords = new HashMap<>();
        bodyLemmas.entrySet().stream().forEach(w -> totalWords.put(w.getKey(), w.getValue() * 0.8f));
        titleLemmas.entrySet().stream().forEach(w -> totalWords.put(w.getKey(), w.getValue() + totalWords.getOrDefault(w.getKey(), 0f)));

        saveLemma(totalWords, page, nodePage);
        response.setResult(true);
        return response;
    }




    public HashSet<String> getAllLinks(Document document) {
        Elements elements = document.select("a[href^=\"/\"]");
        HashSet<String> result = new HashSet<>();
        result.addAll(elements.stream().map(e -> e.attr("href")).collect(Collectors.toList()));
        return result;
    }


    public static int getUniqReffsCount() {
        return uniqReffs.size() == 0 ? 0 : uniqReffs.size();
    }

    public static void clearUniqReffs() {
        uniqReffs.clear();
    }

    public String[] getTitleAndBody(Document document) {
        String[] result = new String[2];
        result[0] = document.getElementsByTag("title").text();
        result[1] = document.getElementsByTag("body").text();
        return result;
    }

    public void saveLemma(Map<String, Float> lemmas, PageEntity page, NodePage nodePage) {

        for (Map.Entry word : lemmas.entrySet()) {

            Lemma lemma = lemmaService.findLemmaByLemma(word.getKey().toString());
            if (lemma.getLemma().equals("null")) {
                lemma = new Lemma();
                lemma.setLemma(word.getKey().toString());
                lemma.setFrequency(1);
            } else {
                lemma.setFrequency(lemma.getFrequency() + 1);
            }
            lemmaService.saveLemma(lemma);

            IndexEntity indexEntity = new IndexEntity();
            indexEntity.setPageEntity(page);
            indexEntity.setLemma(lemma);
            indexEntity.setRank((float) word.getValue());
            indexService.save(indexEntity);

            site2LemmaService.saveNewSite2Lemma(lemma, nodePage.getSiteId());


        }
    }

    public PageEntity getPageById(Integer idPage) {
        return pageRepo.getPageById(idPage);
    }


}

