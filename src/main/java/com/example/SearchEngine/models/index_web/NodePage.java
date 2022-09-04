package com.example.SearchEngine.models.index_web;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Getter
@Setter
@Component
@NoArgsConstructor
public class NodePage {

    private String path;
    private String suffix;
    private String prefix;
    private int timeBetweenRequest;
    private int siteId;


    private HashSet<String> refOnChilds = new HashSet<>(0);

    public NodePage(String path) {
        this.path = path;
    }
}
