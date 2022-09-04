package com.example.SearchEngine.services.search;

import com.example.SearchEngine.exceptions.IllegalSearchQueryParamException;
import com.example.SearchEngine.models.index_web.Lemma;
import com.example.SearchEngine.models.index_web.PageEntity;
import com.example.SearchEngine.models.index_web.SiteEntity;
import com.example.SearchEngine.models.search.DataResponse;
import com.example.SearchEngine.models.search.ResponseSearch;
import com.example.SearchEngine.services.index_web.IndexService;
import com.example.SearchEngine.services.index_web.LemmaService;
import com.example.SearchEngine.services.index_web.NodeService;
import com.example.SearchEngine.services.index_web.SiteService;
import com.example.SearchEngine.services.lucene_morphology.LuceneMorphologyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private LuceneMorphologyService luceneMorphologyService;
    private LemmaService lemmaService;
    private SiteService siteService;
    private NodeService nodeService;
    private IndexService indexService;


    @Autowired
    public SearchService(LuceneMorphologyService luceneMorphologyService, LemmaService lemmaService, SiteService siteService, NodeService nodeService, IndexService indexService) {
        this.luceneMorphologyService = luceneMorphologyService;
        this.lemmaService = lemmaService;
        this.siteService = siteService;
        this.nodeService = nodeService;
        this.indexService = indexService;
    }

    public ResponseSearch getResponseSearch(String query, String pathSite, int percentFrequencyLemma, int offset, int limit) throws IllegalSearchQueryParamException {
        SiteEntity site = null;
        if (pathSite != null){
            site = siteService.findSiteIdByPath(pathSite);
            if (site == null) {
                throw new IllegalSearchQueryParamException("Site with this path is not indexed in our API. Try other path.");
            }
        }

        ResponseSearch responseSearch = new ResponseSearch();
        int maxFrequency = siteService.countAllPagesInDB() * percentFrequencyLemma / 100;
        Set<String> wordsInQuery = luceneMorphologyService.getLemmaSet(query);
        Set<Lemma> sortedSetLemmas = wordsInQuery.stream()
                .map(w -> lemmaService.findLemmaByLemma(w))
                .filter(l -> !l.getLemma().equals("null"))
                .filter(l -> l.getFrequency() < maxFrequency)
                .sorted(Comparator.comparing(Lemma::getFrequency))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        int siteId = site != null? site.getId() : 0;
        Set<Integer> idPagesFiltered = getFilteredPagesByRareLemma(sortedSetLemmas, siteId); //собрали общие страницы для всех поисковых лемм
        if (idPagesFiltered.size() == 0) {
            responseSearch.setResult(true);
            responseSearch.setCount(0);
            responseSearch.setDataResponse(new DataResponse[0]);
            return responseSearch;
        }

        Map<Integer, Float> relevanceAbsolutPages = getPagesWithRelevance(idPagesFiltered, sortedSetLemmas);

        List<DataResponse> dataResponses = getDataResponses(relevanceAbsolutPages, site, sortedSetLemmas, offset, limit)
                .stream()
                .sorted(DataResponse::compareByRelevance).toList();

        responseSearch.setResult(true);
        responseSearch.setCount(relevanceAbsolutPages.size());
        responseSearch.setDataResponse(dataResponses.toArray(new DataResponse[0]));
        return responseSearch;
    }


    private List<DataResponse> getDataResponses(Map<Integer, Float> relevanceAbsolutPages,
                                                SiteEntity site, Set<Lemma> sortedSetLemmas, int offset, int limit) {
        List<DataResponse> responseList = new ArrayList<>();
        int count = -1;
        for (Map.Entry element : relevanceAbsolutPages.entrySet()) {
           count++;
           if (count < offset){
                continue;
            }
           if (count > offset + limit-1){
               break;
           }
            PageEntity page = nodeService.getPageById((int) element.getKey());
            if (page == null) {
                continue;
            }
            String content = page.getContent();

            String snippet = getSnippet(content, sortedSetLemmas);

           SiteEntity siteResult = site != null? site : siteService.getSiteById (page.getSite_id());
            DataResponse dataResponse = new DataResponse();
            dataResponse.setSite(siteResult.getUri());
            dataResponse.setSiteName(siteResult.getName());
            dataResponse.setUri(page.getPath());
            dataResponse.setSnippet(snippet);
            dataResponse.setRelevance((float) element.getValue());
            responseList.add(dataResponse);
        }
        return responseList;
    }

    public String getSnippet(String text, Set<Lemma> lemmaInQuery) {
        if (lemmaInQuery.size() == 0){
            return null;
        }
        String input = text.toLowerCase();
        Map<String, Integer> snippetsOnPage = new HashMap<>();
        int startSearch = 0;
        Set<String> cutWords = new HashSet<>();
        for (;;){
            int countMatchesSearchWords = 0;
            int numberFirstWord = input.length();
            for (String word : lemmaInQuery.stream().map(Lemma::getLemma).toList()) {
                String word1 = word.length() < 2? word : word.substring(0, word.length() - 2);
                cutWords.add(word1);
                int number = StringUtils.indexOfIgnoreCase(input, word1, startSearch);
                if (number != -1) {
                    numberFirstWord = numberFirstWord < number ? numberFirstWord : number;
                }
            }
            if (numberFirstWord == input.length()){
                break;
            }
            int stop = input.indexOf("<", numberFirstWord);
            int stopCustom = numberFirstWord + 700;
            stop = stop == -1?
                    (Math.min(stopCustom, input.length()))
                    : (Math.min(stop, (stopCustom)));

            String result = input.substring(numberFirstWord, stop);

            for (String word : cutWords) {
                countMatchesSearchWords += StringUtils.countMatches(result, word);
                result = result.replaceAll(word,"<b>" + word + "<b>");
            }
            snippetsOnPage.put(result, countMatchesSearchWords);
            startSearch = stop;
        }
        return  snippetsOnPage.size() == 0? null : snippetsOnPage.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null).getKey();
    }


    private Map<Integer, Float> getPagesWithRelevance(Set<Integer> idPagesFiltered, Set<Lemma> sortedSetLemmas) {
        Map<Integer, Float> relevanceAbsolutPages = new HashMap<>();
        float maxRelevance = 0f;
        for (Integer pageId : idPagesFiltered) {
            float rankPage = 0f;
            for (Lemma lemma : sortedSetLemmas) {
                rankPage += indexService.findRankByLemmaAndPage(pageId, lemma.getId());
            }
            relevanceAbsolutPages.put(pageId, rankPage);
            maxRelevance = rankPage > maxRelevance ? rankPage : maxRelevance;
        }

        float finalMaxRelevance = maxRelevance;
        return relevanceAbsolutPages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((e1, e2) -> e2.compareTo(e1)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() / finalMaxRelevance, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private Set<Integer> getFilteredPagesByRareLemma(Set<Lemma> sortedSet, int idSite) {
        Set<Integer> idPagesFiltered = new HashSet<>();
        int count = 0;
        for (Lemma lemma : sortedSet) {
            count++;
            Set<Integer> pagesForLemma = lemmaService.findPagesForLemma(lemma.getId(), idSite);
            if (count == 1) {
                idPagesFiltered.addAll(pagesForLemma);
                continue;
            }
            idPagesFiltered.retainAll(pagesForLemma);
        }
        return idPagesFiltered;
    }
}
