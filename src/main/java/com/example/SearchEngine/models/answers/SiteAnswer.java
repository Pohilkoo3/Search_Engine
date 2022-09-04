package com.example.SearchEngine.models.answers;


import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
@Getter
@Setter
public class SiteAnswer {


    private int code;

    private Document document;


    private Exception exception;


}
