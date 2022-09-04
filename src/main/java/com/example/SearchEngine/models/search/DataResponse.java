package com.example.SearchEngine.models.search;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DataResponse {

    private String site;    //http://www.lenta.ru
    private String siteName;
    private String uri;     ///news/23
    private String snippet; //Фрагмент тека, в котором найдены совпадения, выделенные жирным.
    private float relevance;

    public static int compareByRelevance(DataResponse d1, DataResponse d2){
        return Float.compare(d2.getRelevance(), d1.getRelevance());
    }

}
